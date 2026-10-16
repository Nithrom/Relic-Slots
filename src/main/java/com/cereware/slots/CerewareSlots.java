package com.cereware.slots;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

/** Librería CereWare Slots: pantalla común de huecos extra (tecla C) para los mods CereWare. */
public class CerewareSlots implements ModInitializer {
	public static final String MOD_ID = "cerewareslots";
	public static final MenuType<SlotsMenu> MENU = Registry.register(BuiltInRegistries.MENU,
		Identifier.fromNamespaceAndPath(MOD_ID, "slots"), new MenuType<>(SlotsMenu::new, FeatureFlags.VANILLA_SET));

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.serverboundPlay().register(OpenSlotsPayload.TYPE, OpenSlotsPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(OpenSlotsPayload.TYPE, (payload, context) ->
			context.player().openMenu(new SimpleMenuProvider((id, inv, p) -> new SlotsMenu(id, inv, true),
				Component.translatable("gui.cerewareslots.title"))));
	}
}
