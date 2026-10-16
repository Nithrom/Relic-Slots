# Relic Slots (CereWare Slots)

Librería Fabric (Minecraft 26.3) usada por los mods de CereWare: añade la pantalla común **Reliquias** (tecla **C**) donde cada mod registra su hueco extra (Beacon, Conduit, Tótem).
No hace falta instalarla aparte: va incluida dentro de cada mod.

## Uso para mods
```java
CerewareSlotsApi.register(new SlotDefinition(id, título, ítemSilueta, stack -> ..., maxStack, orden, () -> conservarAlMorir));
CerewareSlotsApi.getStack(player, id);
```

## Licencia
LGPL-3.0-only. © CereWare
