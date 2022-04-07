# MapBox

Esse aplicativo foi desenvolvido como um desafio técnico para um processo seletivo do IEL/Ford.

# Sobre

Esse é um aplicativo que usa o [Mapbox Maps SDK](https://docs.mapbox.com/android/maps/guides/) para mostrar a localização do usuário, adicionar e salvar pins de localização e mostrar todos os pins salvos.

Para desenvolver este app foram usadas as seguintes tecnologias:

- Arquitetura MVVM
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=pt-br)
- [Room](https://developer.android.com/training/data-storage/room) para persistencia de dados.
- [Activity](https://developer.android.com/guide/components/activities/intro-activities?hl=pt-br) única e múltiplos [Fragments](https://developer.android.com/guide/fragments?gclid=Cj0KCQiAjc2QBhDgARIsAMc3SqST_pEQDdcBxiO-1eEH4fAZXAUin2feUuQHSvjaCd8q5o78ld8KOrwaAqYOEALw_wcB&gclsrc=aw.ds)
- [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) para navegar entre os fragments.
- [Data Binding](https://developer.android.com/topic/libraries/data-binding) para víncular os dados e a UI.
- Kotlin [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) para tarefas assíncronas.

# Capturas de tela

![Home-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Tela%20Inicial.png) ![Options-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Op%C3%A7%C3%B5es.png) ![Add-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Adicionar%20Pin.png) ![Show-screen](https://github.com/michael-pessoni/MapBox/blob/master/screenshots/Visualizar%20pins.png)

# TODO

- Implementar testes
- Refatorar a MapViewModel para um código mais legível
-  Implementar novas funcionalidades
