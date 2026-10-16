package com.cereware.slots.api;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Un hueco extra de la pantalla CereWare (tecla C).
 *
 * @param id          identificador único (p. ej. beaconsreforged:beacon)
 * @param title       título que se muestra encima del hueco
 * @param silhouette  ítem que se dibuja en gris cuando el hueco está vacío
 * @param accepts     qué ítems admite
 * @param maxStack    máximo de unidades (0 = el máximo propio del ítem)
 * @param order       posición (menor = más a la izquierda)
 * @param keepOnDeath si devuelve true, el contenido se conserva al morir
 * @param silhouetteSprite sprite plano (estilo vanilla, gris 85,85,85) para el hueco vacío; si es null se dibuja el ítem apagado
 */
public record SlotDefinition(Identifier id, Component title, Item silhouette, Predicate<ItemStack> accepts,
                             int maxStack, int order, BooleanSupplier keepOnDeath, @Nullable Identifier silhouetteSprite) {
	public SlotDefinition(Identifier id, Component title, Item silhouette, Predicate<ItemStack> accepts,
	                      int maxStack, int order, BooleanSupplier keepOnDeath) {
		this(id, title, silhouette, accepts, maxStack, order, keepOnDeath, null);
	}

	public int maxFor(ItemStack stack) {
		return maxStack > 0 ? Math.min(maxStack, stack.getMaxStackSize()) : stack.getMaxStackSize();
	}
}
