# PokemonList

Aplicación Android desarrollada en Kotlin que muestra una lista de Pokémon obtenida desde una API pública. Este proyecto implementa la arquitectura MVVM y utiliza LiveData y ViewModel para gestionar los datos y la UI.

## Características

- **Consumo de API**: Obtiene datos en tiempo real de la [PokeAPI](https://pokeapi.co/).
- **Arquitectura MVVM**: Implementación de Model-View-ViewModel para una separación clara de responsabilidades.
- **LiveData y ViewModel**: Para manejar datos observables y persistencia durante cambios de configuración.
- **Almacenamiento Local**: Uso de SharedPreferences para guardar datos simples de forma local.

## Tecnologías Utilizadas

- Kotlin
- Android Jetpack (LiveData, ViewModel)
- Retrofit para llamadas HTTP
- Glide para carga de imágenes
- SharedPreferences para almacenamiento local

## Capturas de Pantalla

![appimagen1](https://github.com/user-attachments/assets/8621cfce-ff94-4c5a-8967-188d165f1a20)
![appImagen2](https://github.com/user-attachments/assets/6e44c260-e192-43ac-bb7e-b539036049f9)


## Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/jusebcasan/PokemonList.git
