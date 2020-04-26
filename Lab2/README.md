# Locate Me: A Tiny Indoor Positioning System

## Abstract

This is an Android application which realized the following functionalities:

- Declarative UI with **[Jetpack Compose](https://developer.android.com/jetpack/compose)**
- Wi-Fi scanning
- Indoor positioning with backend service based on **Python** and **TCP socket**

Before detailed introduction, three related questions are worth discussing:

- Q: Why is necessary to record all the measured value rather than only the average value?

  A: First of all, an average value is useless. In practice we always want to analyze Wi-Fi scanning results to estimate the indoor position or to optimize the placement of Wi-Fi routers. An averaged RSSI could not provide valid information for those purposes. Second, Wi-Fi scanning is usually very noisy. Averaging may lead to huge errors and there are more analysis to do beyond that. 

- Q: Besides the Wi-Fi signal strength, what other information of the Routers can be got in the test?

  A: SSID, BSSID, MAC, Link speed, Frequency and Channel.

- Q: Why does the scanning need to be operated in thread `scanThread`?

  A: The question is a little bit ambiguous here. I think it is because we want it to be running in background continuously and parallelly. 

## Dependency

Main dependencies are:

- IDE: Android Studio 4.1 Canary 6, a preview release version allowing me to enable Jetpack Compose.
- Frontend
  - Jetpack Compose 0.1.0-dev06
  - AndroidX
  - Gson
- Backend
  - scikit-learn
  - pandas

As suggested, I use the latest version of the software. **No plugins or modules beyond official libraries are used in this project and every single line of code are written by me starting from scratch**. 

## Basic Structure

As a follow-up of [Easy Dialer](../../Lab1/README.md), **Locate Me** is also a Jetpack Compose App. I find it very simple to construct declarative UI with Jetpack Compose although it is at this stage buggy and short of official examples. 

To enable Wi-Fi scanning, I build a simple service that can run in the background stably with the help of [Official Documents](https://developer.android.google.cn/guide/topics/connectivity/wifi-scan?hl=zh-cn). Every 5 seconds the list of information about Wi-Fi routers nearby will be updated. 

Further more, the indoor positioning system are implemented. Due to the limit of time, I could not propose a new algorithm but refer to paper **[An Enhanced WiFi Indoor Localization System Based on Machine Learning](https://ieeexplore.ieee.org/abstract/document/7743586/)**, which is published in 2016. This fingerprint-based algorithm attempts to find the perfect match between the user fingerprint and pre-defined set of grid points with some machine learning methods like **PCA** and **KNN Regression**. 

The whole system is designed into two part: **Android client** and **Python server**. Locate Me application allows user to collect data of scanning results as well as the relative position and send them to the server. The server, on the other hand, can reply the user with stored data and an estimated position the user is. **Pandas** library takes in charge of data in the backend, and I use simple **TCP sockets** to connect the client and server.

This system is far from practicable since a lot of code is roughly-written given that the time is limited. 

## Scanning

First of all, I define class `WifiScanner` to manage scanning service. It realizes methods including setting up a `WifiManager`, defining what to do when scanning results are received, and starting the service. 

`WifiScanner` will take a lambda function parameter `doOnResults()`. When the `BroadcastReceiver` receives scanning results, `doOnResults()` will be invoked and update access points and information of the current connection. It is very convenient to realize data update with Jetpack Compose (you don't need to do nothing but add an annotation `@Model` actually). 

```kotlin
private fun doOnResults(connectionInfo: WifiInfo, results: List<ScanResult>) {
    LocateMeStatus.accessPoints.clear()

    for (result in results) {
        LocateMeStatus.accessPoints.add(
            AccessPoint(
                ssid = result.BSSID,
                name = if ((result.SSID).isEmpty()) "<Hidden AP>" else result.SSID,
                strength = result.level
            )
        )
    }

    LocateMeStatus.currentConnection = connectionInfo
}
```

When we want to start scanning, we can invoke `startScanner()`, which will create a `Handler` to deal with scanning results outside the UI thread. The scanner will invoke `wifiManager.startScan()` every 5 seconds in order to update scanning results.

```
fun startScanner(attachHandler: Boolean = true) {
	if (attachHandler) {
    	val handler = Handler()
        val runnableCode = object : Runnable {
        	override fun run() {
				wifiManager.startScan()
                handler.postDelayed(this, 5000)
            }
        }
            
        handler.post(runnableCode)
    } else {
    	wifiManager.startScan()
    }
}
```

To start scanning, the application should first ask for permissions. When all permissions are granted, `WifiScanner` will be set up and invoke `startScanner()`. This series of actions is performed in `onResume()` of `MainActivity` since we want the service runs when the application is in the foreground. 

To present scanning results, a `VerticalScroller()` is used and the SSID, BSSID and strength of every result are displayed. Besides, I fix a box in the top to display information of the current connection. Since the UI design patterns are similar to [Easy Dialer](../../Lab1/README.md), I am not going to discuss it in detail. Feel free to review code!

<img src="C:/Users/xzh87/OneDrive/Reference/S3-2/EE447/EE447_Lab/Lab2/fig/main_screen.png" alt="Main Screen" style="zoom:20%;" />

Another thing worth mentioning is that I noticed [Android added platform support](https://android-developers.googleblog.com/2018/03/previewing-android-p.html) for the **IEEE 802.11mc** Wi-Fi protocol -- also known as Wi-Fi Round-Trip-Time (RTT) -- after Android 9.0. However, due to the lack of support on 802.11mc, none of routers scanned in my house support this feature.

## Indoor Positioning

The procedure to accomplish indoor positioning is roughly like this:

- Collect and store enough Wi-Fi fingerprint data to train a model.
- Estimate the user's location with the model using the Wi-Fi fingerprint

### Client

#### Fingerprint Sending

The Android client needs to realize methods to allow the user to report his or her Wi-Fi fingerprint as well as the relative position in the building. The relative position is represented by two 0-1 floats indicating the relative latitude and longitude the user is in the building. The user can input this with a `Slider()`. 

<img src="C:/Users/xzh87/OneDrive/Reference/S3-2/EE447/EE447_Lab/Lab2/fig/add_fingerprint.png" alt="Add Fingerprint" style="zoom:20%;" />

When the user clicks the button 'Add', our Android client will send the data to our Python server with TCP sockets. 

```kotlin
confirmButton = {
    Button(
        onClick = {
            val fingerprint = Fingerprint(
                location = Location(
                    latitude = latitude,
                    longitude = longitude
                ),
                accessPoints = LocateMeStatus.accessPoints
            )
            val jsonFingerprint = Gson().toJson(fingerprint)
            var estimatedLocation = Location()
            
            val thread = Thread {
                estimatedLocation = sendFingerprint(
                    jsonFingerprint = jsonFingerprint
                )
            }

            thread.start()
            thread.join(500)
            
            dialogShow = false
        }
    ) {
        Text(text = "Add")
    }
}
```

I use **Gson** library to manage data formats. `Location` class encapsulates both fingerprint and position information for the purpose of convenience. 

Except for adding the new fingerprint to its database, our python server will respond with an estimated position predicted by the trained model (*I do it this way just because I don't want to design another page to request for estimation*). 

```kotlin
fun sendFingerprint(jsonFingerprint: String): Location {
    val clientSocket = Socket(serverHost, serverPort)
    val inputStream = clientSocket.getInputStream()
    val outputStream = clientSocket.getOutputStream()
    val localHost = InetAddress.getLocalHost()
    val itemType = object : TypeToken<Location>() {}.type
    val receipt: String
    
    outputStream.write(jsonFingerprint.toByteArray());
    outputStream.flush();
    clientSocket.shutdownOutput()
    
    receipt = String(inputStream.readBytes(), charset = charset("utf-8"))

    outputStream.close()
    inputStream.close()
    clientSocket.close()
    
    return Gson().fromJson(receipt, itemType)
}
```

#### Stored Positions Fetching

Since we do not store data in the mobile device as [Jetpack Compose will breaks the compiler of Room](https://stackoverflow.com/questions/59277354/jetpack-compose-breaks-room-compiler), I realize another method to request stored positions in the server. This allows the user to check how many fingerprints are recorded. 

<img src="C:/Users/xzh87/OneDrive/Reference/S3-2/EE447/EE447_Lab/Lab2/fig/stored_position_screen.png" alt="Stored Position Screen" style="zoom:20%;" />

The method to realize it using TCP sockets is similar but easier. 

### Server

In the backend, a Python server will store the received fingerprint to a simple database and call back the estimated position which is predicted by our trained model with previous data. 

First of all, we need to process those received data. A very important step is to create *real fingerprint* represented by a vector. To accomplish this, I map the signal strengths collected into a 100 dimensional vector by hashing and normalizing. **Pandas** will then help to store fingerprints into a *csv* file (for simplicity). 

```python
for accessPoint in jsonDictionary['accessPoints']:
    fingerprint[hash(accessPoint['name']) % 100] = abs(accessPoint['strength'])

maxBit = max(fingerprint)
fingerprint = [float(i) / maxBit for i in fingerprint]

// ...

fingerprintDF.to_csv("dbFingerprint.csv", mode='a', header=False, index=False)
```

Further more, I define a class `Estimator` to deal with the trained model. It consists of two components: a **PCA** model to reduce dimensionality of data and a **KNN Regressor** model to do the prediction. I make it really simple here as it is just a demo. 

```python
class Estimator():
    def __init__(self, dataset):
        DF = pd.read_csv(dataset)
        self.X = DF.iloc[:, 0:100].to_numpy()
        self.Y1 = DF.iloc[:, 100].to_numpy()
        self.Y2 = DF.iloc[:, 101].to_numpy()

        if self.X.shape[0] > self.X.shape[1]:
            self.pca = PCA(n_components='mle', svd_solver='full')
        else:
            self.pca = PCA()

        self.neighLatitude = KNeighborsRegressor(n_neighbors=3)
        self.neighLongitude = KNeighborsRegressor(n_neighbors=3)

        self.fit()

    def update(self, dataset):
        DF = pd.read_csv(dataset)
        self.X = DF.iloc[:, 0:100].to_numpy()
        self.Y1 = DF.iloc[:, 100].to_numpy()
        self.Y2 = DF.iloc[:, 101].to_numpy()

        self.fit()
        
        print("Database is updated")

    def estimate(self, x):
        estimatedLatitude = self.neighLatitude.predict(x)[0]
        estimatedLongitude = self.neighLongitude.predict(x)[0]

        return {'latitude': estimatedLatitude, 'longitude': estimatedLongitude}
        
    def fit(self):
        self.pca.fit(self.X)
        self.neighLatitude.fit(self.X, self.Y1)
        self.neighLongitude.fit(self.X, self.Y2)
```

## Conclusion

In this project, I implement some interesting things related to other areas such as **computer networking** and **machine learning**, which are courses I am actually taking. I am glad I eventually built up the entire system in a couple of days although it is far from useable.

