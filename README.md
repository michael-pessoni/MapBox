# MapBox

This app was developed as a technical challenge for a recuitment process at IEL/Ford.

# About this

This is an app that uses [Mapbox Maps SDK](https://docs.mapbox.com/android/maps/guides/) to show user's location, add and save location pins and show all saved pins.

To develop it the following technologies were used:

- MVVM Architecture
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=pt-br)
- [Room](https://developer.android.com/training/data-storage/room) for data persistence.
- Single [Activity](https://developer.android.com/guide/components/activities/intro-activities?hl=pt-br), multiple [Fragments](https://developer.android.com/guide/fragments?gclid=Cj0KCQiAjc2QBhDgARIsAMc3SqST_pEQDdcBxiO-1eEH4fAZXAUin2feUuQHSvjaCd8q5o78ld8KOrwaAqYOEALw_wcB&gclsrc=aw.ds)
- [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) to navigate between fragments.
- [Data Binding](https://developer.android.com/topic/libraries/data-binding) to vinculate the UI and the data.
- Kotlin [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for assychornous tasks.

# Screenshots

![Home-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Tela%20Inicial.png) ![Options-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Op%C3%A7%C3%B5es.png) ![Add-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Adicionar%20Pin.png) ![Show-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Visualizar%20pins.png)

# TODO

- Implement tests
- Refactor MapViewModel for a more readable code
- Implement new functionalities 
