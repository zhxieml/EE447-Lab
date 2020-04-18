# Easy Dialer: A Simple App with Jetpack Compose

## Abstract

I am again faced with Android development and somehow I have a very mixed feeling about this. Previous experience was really horrible: all the passions in the early stage faded when I started designing some architectures. MVC, MVP, MVVM and so on. Which one should I choose?

And finally, I had a solution. I mixed them up into some "delicate" framework by following others' tutorials. I was told to do this and that, never thinking about what is actually appropriate for me. Suddenly, after I had "stolen" tons of fancy Github plugins to fill up my "fragments", I was overwhelmed with the messy logic in my project. I lost my passions, letting it go. 

Therefore, this time I really want to start from the bottom. Instead of browsing for blogs and tutorials, I go straight to the official document, trying to realize my ideas with simplest tools. **No plugins or modules beyond official libraries are used in this project**. 

## About Jetpack Compose

After surfing online for newest Android development framework, I had something that really interests me: Jetpack Compose. It was in a [youtube video](https://www.youtube.com/watch?v=VsStyq4Lzxo), where Google folks are sharing the latest development about declarative UI patterns for native Android. Since I knew little about declarative programming and related frameworks like React Native and Flutter, I was shocked by how elegant and simple Jetpack Compose is. Getting rid of all those XML layouts is exactly what I want. 

Formally, **Jetpack Compose is part of Android Jetpack framework, a suite of libraries, tools, and guidance to help developers write high-quality apps more easily**. It is heavily inspired by Flutter, and inherit its core design philosophy: **Everything is a widget**. More complex widgets have been broken up into very elemental widgets with clear responsibilities. Instead of using verbose XML files to design our UI, we can mix Kotlin code with UI widgets in a declarative way. 

Another thing to notice is that **Jetpack Compose is currently in Developer Preview**, which means the APIs are changing all the time with incomplete documents and lack of tutorials. The only official sample I can find is [JetNews](https://github.com/android/compose-samples/tree/master/JetNews), a toy example with basic functions and static data. Moreover, [it even breaks the compiler of Room](https://stackoverflow.com/questions/59277354/jetpack-compose-breaks-room-compiler), another Jetpack library which provides an abstraction layer over SQLite to allow for more robust database access. I feel kind of regretted for using Jetpack Compose instead of Flutter in this lab. 

But anyway, I still learn a lot in this lab, especially about declarative programming and debugging. And I enjoy it!

## Dependency

As suggested, I use the latest version of the software.

- Android Studio 4.1 Canary 5, a preview release version allowing me to enable Jetpack Compose.
- Kotlin 1.3.72
- Jetpack Compose 0.1.0-dev06
- AndroidX

## Hello World!

To get started, we show how powerful Jetpack Compose is with the classical "Hello World" example. 

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Greeting("World")
            }
        }
    }
}
```

As normal Android projects, we set the entry of application in our `MainActivity`. But here we do not `setContentView()`: there is no view in Jetpack Compose actually. Widgets are just functions that draw onto the canvas!

Instead, we use `setContent()` to design our UI patterns. It takes a **composable** lambda function as its input, which here is `MaterialTheme()`. Since the lambda function is the last parameter of `setContent()`, we can directly write it in the curly braces according to Kotlin's property. 

Now let's take a look at `MaterialTheme()` from `androidx.ui.material`, a library to support material design principles. 

```kotlin
@Composable
fun MaterialTheme(
    colors: ColorPalette = ColorPalette(),
    typography: Typography = Typography(),
    children: @Composable() () -> Unit
) {
    ProvideColorPalette(colors) {
        TypographyAmbient.Provider(value = typography) {
            CurrentTextStyleProvider(value = typography.body1, children = children)
        }
    }
}
```

To create a composable function, we need to add the `@Composable` annotation to the function name. As we can notice, `MaterialTheme()` also takes a composable lambda function `children`, and wraps it with other composable functions to realize our desired UI including colors and typography. In our example, we wrap the function `Greeting("World")` with default material design pattern. And our `Greeting()` function only takes responsibility of displaying the text. 

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
```

This basically shows what composable functions are: they are just widgets over widgets! However, composable functions are not just about widgets themselves, they are also about **data flow**. This will be discussed in detailedly in the following sections. 

And now, we are good to go.

<img src="/fig/hello_world.png" alt="hello world" style="zoom: 25%;" />

## Easy Dialer

To further explore Jetpack Compose, I create a simple dialer app with Compose toolkit. The functions it realizes include:

- Dial with phone number
- Add emergency contacts 
- Dial emergency contacts 

### Basic Structure

Although the application is very simple, I still feel it necessary to follow some pattern. The traditional MVVM architecture require a `repository --> view model --> view` dataflow, which is kind of frustrating. 

Thanks to Jetpack Compose, we can simplified it a lot by simplifying `View Model` part. We use `@Model` annotation to label observable objects. More magically, we do not need to listen to them and act when they are changed. Just use them! **When the data are changed, composable functions will automatically decompose and then compose with updated data value**. 

As for `View` part, we construct UI with composable widgets layer by layer. This provides us great ability of reuse. For example, the main screen can be constructed by first wrapping a `MaterialTheme()`, and filling it with some `Row()` and `Column()` to control position, and finally adding some `Text()` to display information. In this way, we avoid using too much code. Traditional fragments usually inherit other fragments to realize fancy functionality. With composable functions, we can instead combine small widgets into big ones.

<img src="/fig/too_much_code" alt="too much code" style="zoom:50%;" />

And finally, I have something to say about `Repository` part. At first I tried to use [Room](https://developer.android.com/training/data-storage/room) to construct simple database so that our emergency contacts can persist in our phone. Unfortunately, after I finish all the database and DAO, I find my project unbuildable. And then I find out [Jetpack Compose breaks Room Compiler](https://stackoverflow.com/questions/59277354/jetpack-compose-breaks-room-compiler). I was shocked and regretted. Therefore, in this project I only play with static data. 

### MainActivity

`MainActivity` is our only activity and the entry to our application. We override `onCreate()` and `onActivityResult()` here. 

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyDialerApp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == dialRequestCode) {
            EasyDialerStatus.isCalling = false
            EasyDialerStatus.phoneNumber = ""
        }
    }
}
```

`onCreate()` is nothing special. Rather, we focus on `onActivityResult()`. As I mentioned, `MainActivity` is the only activity here. But since we need to dial, which will create another activity, we must resume states when we hang up.

### EasyDialerApp

In `EasyDialerApp()`, we start to implement concrete functionality. 

```kotlin
@Composable
fun EasyDialerApp() {
    checkPermissions(context = ContextAmbient.current)
    
    MaterialTheme(colors = mainThemeColors, typography = mainThemeTypography) {
        AppContent()
    }
}
```

To make sure users can use our app to dial correctly, we first check permissions and access unauthorized permissions. Then, we fill contents with our custom colors and typography. This makes sure all contents  are in the same style. 

### HomeScreen

In `HomeScreen()`, we define our UI of 

