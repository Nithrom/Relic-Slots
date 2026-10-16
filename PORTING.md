# Cómo actualizar los mods CereWare a una nueva versión de Minecraft

Aplica a: CereWare Slots, Beacons Reforged, Conduit Reforged, Totem Slot.

## 1. Versiones (un único sitio por proyecto)
En `gradle.properties` de **cada** proyecto: `minecraft_version`, `loader_version`, `loom_version`, `fabric_api_version`
(valores en https://fabricmc.net/develop). En `fabric.mod.json`: `"minecraft": "~26.x"`.
Actualiza **primero CereWare Slots** (los demás la compilan desde la carpeta hermana con `includeBuild`).

## 2. Compilar y arreglar
`gradlew build` en cada proyecto. Si algo no compila, casi siempre está en estos puntos (los que más cambian entre versiones):

| Zona | Dónde |
|---|---|
| Dibujado de pantallas (`GuiGraphicsExtractor`, `extract*`) | `*Screen.java` de cada mod y `SlotsScreen` |
| Ratón/teclado (`InputConstants`, `MouseButtonEvent`) | `FullBeaconScreen`, `CerewareSlotsClient` |
| Guardado de datos (`ValueInput/ValueOutput`, data components) | mixins de block entity (beacon, conduit) |
| Mixins (nombres de métodos vanilla) | carpetas `mixin/` (pocos y comentados) |
| Tablas de botín (`copy_components`) | `data/minecraft/loot_table/blocks/*.json` |

## 3. Probar
`gradlew runServer` (que arranque sin errores de mixin) y `gradlew runClient` en cada mod;
después los tres juntos copiando los .jar a `run/mods` de uno de ellos.

## 4. Publicar
Subir `version` en `gradle.properties` y generar el .jar (`gradlew build` → `build/libs/`).
