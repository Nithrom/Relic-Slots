package com.cereware.slots.api;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/** API pública: registrar huecos y leer/escribir su contenido. */
public final class CerewareSlotsApi {
	private static final List<SlotDefinition> DEFINITIONS = new ArrayList<>();

	/** Contenido de todos los huecos del jugador (persistente; se copia al reaparecer). */
	public static final AttachmentType<Map<Identifier, ItemStack>> CONTENTS = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath("cerewareslots", "contents"),
		b -> b.persistent(Codec.unboundedMap(Identifier.CODEC, ItemStack.OPTIONAL_CODEC)).copyOnDeath());

	private CerewareSlotsApi() {}

	/** Llamar desde onInitialize (común: cliente y servidor). */
	public static synchronized void register(SlotDefinition def) {
		if (DEFINITIONS.stream().anyMatch(d -> d.id().equals(def.id()))) return;
		DEFINITIONS.add(def);
		DEFINITIONS.sort(Comparator.comparingInt(SlotDefinition::order).thenComparing(d -> d.id().toString()));
	}

	public static List<SlotDefinition> definitions() {
		return List.copyOf(DEFINITIONS);
	}

	public static @Nullable SlotDefinition get(Identifier id) {
		for (SlotDefinition d : DEFINITIONS) if (d.id().equals(id)) return d;
		return null;
	}

	/** Primer hueco que admite este ítem. */
	public static @Nullable SlotDefinition findFor(ItemStack stack) {
		if (stack.isEmpty()) return null;
		for (SlotDefinition d : DEFINITIONS) if (d.accepts().test(stack)) return d;
		return null;
	}

	public static ItemStack getStack(Player player, Identifier id) {
		Map<Identifier, ItemStack> map = player.getAttached(CONTENTS);
		if (map == null) return ItemStack.EMPTY;
		ItemStack s = map.get(id);
		return s == null ? ItemStack.EMPTY : s;
	}

	public static void setStack(Player player, Identifier id, ItemStack stack) {
		Map<Identifier, ItemStack> current = player.getAttached(CONTENTS);
		Map<Identifier, ItemStack> map = current == null ? new HashMap<>() : new HashMap<>(current);
		if (stack.isEmpty()) map.remove(id);
		else map.put(id, stack);
		if (map.isEmpty()) player.removeAttached(CONTENTS);
		else player.setAttached(CONTENTS, map);
	}
}
