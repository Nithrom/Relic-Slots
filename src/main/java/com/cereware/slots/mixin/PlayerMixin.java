package com.cereware.slots.mixin;

import com.cereware.slots.api.CerewareSlotsApi;
import com.cereware.slots.api.SlotDefinition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Al morir, suelta el contenido de los huecos en la misma fase que el inventario (compatible con mods de tumbas). */
@Mixin(Player.class)
public abstract class PlayerMixin {
	@Inject(method = "dropEquipment", at = @At("TAIL"))
	private void cerewareslots$dropSlots(ServerLevel level, CallbackInfo ci) {
		if (level.getGameRules().get(GameRules.KEEP_INVENTORY)) return;
		Player self = (Player) (Object) this;
		for (SlotDefinition def : CerewareSlotsApi.definitions()) {
			if (def.keepOnDeath().getAsBoolean()) continue;
			ItemStack stack = CerewareSlotsApi.getStack(self, def.id());
			if (stack.isEmpty()) continue;
			ItemEntity entity = self.createItemStackToDrop(stack, true, false);
			if (entity != null) level.addFreshEntity(entity);
			CerewareSlotsApi.setStack(self, def.id(), ItemStack.EMPTY);
		}
	}
}
