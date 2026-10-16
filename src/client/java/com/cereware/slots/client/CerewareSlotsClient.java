package com.cereware.slots.client;

import com.cereware.slots.CerewareSlots;
import com.cereware.slots.OpenSlotsPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;

public class CerewareSlotsClient implements ClientModInitializer {
	public static final KeyMapping OPEN = KeyMappingHelper.registerKeyMapping(new KeyMapping(
		"key.cerewareslots.open", InputConstants.Type.KEYBOARD, InputConstants.KEY_C, KeyMapping.Category.INVENTORY));

	@Override
	public void onInitializeClient() {
		MenuScreens.register(CerewareSlots.MENU, SlotsScreen::new);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (OPEN.consumeClick()) {
				if (client.player != null) ClientPlayNetworking.send(OpenSlotsPayload.INSTANCE);
			}
		});
	}
}
