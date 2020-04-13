# Lab1: Easy Dialer

## Some References

- [Official Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Related Codelab](https://github.com/googlecodelabs/android-using-kotlin)
- [Official Guide About Contacts Provider](https://developer.android.com/guide/topics/providers/contacts-provider)
- [Jetpack Compose Playground](https://foso.github.io/Jetpack-Compose-Playground/)
- ~~[Android MVI with Jetpack Compose](https://medium.com/swlh/android-mvi-with-jetpack-compose-b0890f5156ac)~~
- [Diving into Jetpack Compose](https://engineering.q42.nl/android-jetpack-compose/)
- [Writing Android UI Code in Jetpack Compose](https://www.rivu.dev/writing-android-ui-code-in-jetpack-compose/)
- [About onClick()](https://stackoverflow.com/questions/59807648/jetpack-compose-appbaricon-complains-that-functions-which-invoke-composable-fu)
- [应用架构指南](https://developer.android.com/jetpack/docs/guide)
- [Declarative UI](https://flutter.dev/docs/get-started/flutter-for/declarative)
- [Android Jetpack Architecture Components: Getting Started](https://www.raywenderlich.com/6729-android-jetpack-architecture-components-getting-started)

## Some Key Words

- "Once you mastered the idea of building UIs through “composing” components, switching to the Flutter (React) pattern is pretty simple." [Android MVI with Jetpack Compose](https://medium.com/swlh/android-mvi-with-jetpack-compose-b0890f5156ac)
- It is my first time doing some MVI / Declarative Programming
- "What if we can change this situation, giving the `ViewModel` , or way better — the `ViewState`— the power to build the *UI*, and letting the `View` only render it? That’s possible thanks to Jetpack Compose. " [Android MVI with Jetpack Compose](https://medium.com/swlh/android-mvi-with-jetpack-compose-b0890f5156ac)
- "Compose is a reactive UI toolkit entirely developed in Kotlin. Compose looks quite similar to existing UI frameworks like React, Litho or Flutter."
- main goals in mind:
  - **Unbundled from platform releases:** This allows bugs to be fixed and released quickly because it’s independent of new Android releases.
  - **Fewer technology stack flowcharts:** The framework does not force you to use a View or Fragment when creating your UI. Everything is a component and may be composed freely together.
  - **Clarify state ownership and event handling:** One of the most important and complex things to get right in larger applications is handling data flow and state in your UI. Compose makes it clear who is in charge of state and how events should be handled, similar to how React handles this.
  - **Writing less code:** Writing a UI in Android usually requires a LOT of code, especially when creating more complex layouts with a RecyclerView for example. Compose aims to dramatically simplify the way you build your UI.
- "It’s important to realize that Jetpack Compose widgets don’t use views or fragments under the hood, they are just functions that draw onto the canvas."
- "Not using existing views also means Jetpack Compose can’t leverage the currently available views like `android.widget.Button` and must build all widgets from the ground up."
- object -- "Singleton Pattern"
- Tricky in `onClick()`: it is not **Composable**!
- "Here we see a lot less *stuff* in the Jetpack Compose variant. Most of this is due to the Kotlin DSL that was made for Jetpack Compose."