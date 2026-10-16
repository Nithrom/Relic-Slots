package com.cereware.slots;

import com.cereware.slots.api.CerewareSlotsApi;
import com.cereware.slots.api.SlotDefinition;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Container de 1 hueco que lee/escribe directamente el contenido guardado en el jugador. */
public final class SlotContainer implements Container {
	private final Player player;
	private final SlotDefinition def;

	public SlotContainer(Player player, SlotDefinition def) {
		this.player = player;
		this.def = def;
	}

	private ItemStack get() { return CerewareSlotsApi.getStack(player, def.id()); }
	private void set(ItemStack s) { CerewareSlotsApi.setStack(player, def.id(), s); }

	@Override public int getContainerSize() { return 1; }
	@Override public int getMaxStackSize() { return def.maxStack() > 0 ? def.maxStack() : 99; }
	@Override public boolean isEmpty() { return get().isEmpty(); }
	@Override public ItemStack getItem(int slot) { return get(); }

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack s = get();
		if (s.isEmpty() || amount <= 0) return ItemStack.EMPTY;
		ItemStack taken = s.split(amount);
		set(s);
		return taken;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack s = get();
		set(ItemStack.EMPTY);
		return s;
	}

	@Override public void setItem(int slot, ItemStack stack) { set(stack); }
	@Override public void setChanged() { set(get()); }
	@Override public boolean stillValid(Player p) { return p == player; }
	@Override public void clearContent() { set(ItemStack.EMPTY); }
}
