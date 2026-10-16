package com.cereware.slots.mixin;

import com.cereware.slots.api.CerewareSlotsApi;
import com.cereware.slots.api.SlotDefinition;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Shift-clic en el inventario (E) sobre un ítem que admite algún hueco: lo mete en su hueco. */
@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin {
	@Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
	private void cerewareslots$toSlot(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
		if (index < 9 || index > 45) return;
		Slot slot = ((InventoryMenu) (Object) this).slots.get(index);
		ItemStack stack = slot.getItem();
		SlotDefinition def = CerewareSlotsApi.findFor(stack);
		if (def == null) return;
		cir.setReturnValue(ItemStack.EMPTY);
		if (!(player instanceof ServerPlayer sp)) return; // el servidor sincroniza el resultado
		ItemStack current = CerewareSlotsApi.getStack(sp, def.id());
		int max = def.maxFor(stack);
		if (current.isEmpty()) {
			CerewareSlotsApi.setStack(sp, def.id(), stack.split(Math.min(max, stack.getCount())));
		} else if (ItemStack.isSameItemSameComponents(current, stack) && current.getCount() < max) {
			int move = Math.min(max - current.getCount(), stack.getCount());
			ItemStack merged = current.copy();
			merged.grow(move);
			stack.shrink(move);
			CerewareSlotsApi.setStack(sp, def.id(), merged);
		} else if (stack.getCount() == 1 && current.getCount() == 1) {
			CerewareSlotsApi.setStack(sp, def.id(), stack.copy());
			slot.set(current);
			stack = ItemStack.EMPTY;
		} else {
			return;
		}
		slot.setChanged();
		sp.level().playSound(null, sp.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.PLAYERS, 0.8f, 1.2f);
	}
}
