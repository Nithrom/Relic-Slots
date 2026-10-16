package com.cereware.slots;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Cliente → servidor: abrir la pantalla de huecos (tecla C). */
public record OpenSlotsPayload() implements CustomPacketPayload {
	public static final OpenSlotsPayload INSTANCE = new OpenSlotsPayload();
	public static final Type<OpenSlotsPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("cerewareslots", "open"));
	public static final StreamCodec<RegistryFriendlyByteBuf, OpenSlotsPayload> CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
