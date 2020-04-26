from socket import *
from threading import Thread
import json
import pandas as pd
from pandas.io.json import json_normalize
from estimate import Estimator
import numpy as np


serverPort = 12000
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(('', serverPort))
serverSocket.listen(5)

estimator = Estimator("dbFingerprint.csv")

print("The server is ready")

while True:
    connectionSocket, addr = serverSocket.accept()
    sentence = connectionSocket.recv(4096)
    encodedSentence = str(sentence, encoding="utf-8")
    
    print("Receive {}:".format(encodedSentence))

    if encodedSentence == "retrieve":
        DF = pd.read_csv("dbPosition.csv")
        DF.columns = ['numOfAP', 'latitude', 'longitude']

        jsonList = DF.to_json(orient='records')
        print(jsonList)
        connectionSocket.send(bytes(jsonList, encoding="utf-8"))
        
    else:
        jsonDictionary = json.loads(encodedSentence)

        position = [len(jsonDictionary['accessPoints'])]
        fingerprint = [0] * 100

        for accessPoint in jsonDictionary['accessPoints']:
            fingerprint[hash(accessPoint['name']) % 100] = abs(accessPoint['strength'])

        maxBit = max(fingerprint)
        fingerprint = [float(i) / maxBit for i in fingerprint]

        position.append(jsonDictionary['location']['latitude'])
        position.append(jsonDictionary['location']['longitude'])
        fingerprint.append(jsonDictionary['location']['latitude'])
        fingerprint.append(jsonDictionary['location']['longitude'])

        # estimate current location
        estimatedLocation = estimator.estimate(np.matrix([fingerprint[0:100]]))

        locationDF = pd.DataFrame([position])
        locationDF.to_csv("dbPosition.csv", mode='a', header=False, index=False)
        fingerprintDF = pd.DataFrame([fingerprint])
        fingerprintDF.to_csv("dbFingerprint.csv", mode='a', header=False, index=False)

        thread = Thread(estimator.update("dbFingerprint.csv"))
        thread.start()

        connectionSocket.send(bytes(json.dumps(estimatedLocation), encoding="utf-8"))
    
    connectionSocket.close()    


