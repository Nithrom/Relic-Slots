package com.cereware.slots;

import com.cereware.slots.api.CerewareSlotsApi;
import com.cereware.slots.api.SlotDefinition;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Pantalla de la tecla C: un hueco por cada mod registrado + inventario. */
public class SlotsMenu extends AbstractContainerMenu {
	public static final int SLOT_Y = 20;
	private final List<SlotDefinition> defs = CerewareSlotsApi.definitions();

	public SlotsMenu(int id, Inventory inventory) {
		this(id, inventory, false);
	}

	public SlotsMenu(int id, Inventory inventory, boolean server) {
		super(CerewareSlots.MENU, id);
		for (int i = 0; i < defs.size(); i++) {
			SlotDefinition def = defs.get(i);
			Container c = server ? new SlotContainer(inventory.player, def) : new SimpleContainer(1);
			addSlot(new Slot(c, 0, slotX(i, defs.size()), SLOT_Y) {
				@Override public boolean mayPlace(ItemStack stack) { return def.accepts().test(stack); }
				@Override public int getMaxStackSize(ItemStack stack) { return def.maxFor(stack); }
			});
		}
		addStandardInventorySlots(inventory, 8, 51);
	}

	/** Huecos centrados con 50 px de separación. */
	public static int slotX(int i, int n) {
		return 80 + (int) Math.round((i - (n - 1) / 2.0) * 50);
	}

	public List<SlotDefinition> defs() {
		return defs;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = slots.get(index);
		if (!slot.hasItem()) return ItemStack.EMPTY;
		ItemStack stack = slot.getItem();
		ItemStack original = stack.copy();
		int n = defs.size();
		if (index < n) {
			if (!moveItemStackTo(stack, n, slots.size(), true)) return ItemStack.EMPTY;
		} else {
			int target = -1;
			for (int i = 0; i < n; i++) if (defs.get(i).accepts().test(stack)) { target = i; break; }
			if (target < 0) return ItemStack.EMPTY;
			Slot t = slots.get(target);
			if (!moveItemStackTo(stack, target, target + 1, false)) {
				// hueco ocupado con otro ítem de 1 unidad: intercambiar
				if (stack.getCount() == 1 && t.hasItem() && t.getItem().getCount() == 1) {
					ItemStack old = t.getItem();
					t.set(stack.copy());
					slot.set(old);
				}
				return ItemStack.EMPTY;
			}
		}
		if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
		else slot.setChanged();
		return original;
	}
}
