package com.cereware.slots.client;

import com.cereware.slots.SlotsMenu;
import com.cereware.slots.api.SlotDefinition;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/** Pantalla de huecos: título encima de cada hueco y silueta gris del ítem cuando está vacío. */
public class SlotsScreen extends AbstractContainerScreen<SlotsMenu> {
	private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/hopper.png");
	private static final int BG = 0xFFC6C6C6;

	public SlotsScreen(SlotsMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title, 176, 133);
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(g, mouseX, mouseY, partialTick);
		g.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0f, 0f, imageWidth, imageHeight, 256, 256);
		// Tapar los 5 huecos de la tolva y el título, y dibujar los nuestros
		g.fill(leftPos + 7, topPos + 4, leftPos + 169, topPos + 38, BG);
		List<SlotDefinition> defs = menu.defs();
		for (int i = 0; i < defs.size(); i++) {
			int x = leftPos + SlotsMenu.slotX(i, defs.size()), y = topPos + SlotsMenu.SLOT_Y;
			g.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x - 1, y - 1, 43f, 19f, 18, 18, 256, 256);
			if (!menu.getSlot(i).hasItem()) {
				Identifier sprite = defs.get(i).silhouetteSprite();
				if (sprite != null) {
					g.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 16, 16);
				} else {
					g.item(new ItemStack(defs.get(i).silhouette()), x, y);
					g.fill(x, y, x + 16, y + 16, 0xB08B8B8B);
				}
			}
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		List<SlotDefinition> defs = menu.defs();
		for (int i = 0; i < defs.size(); i++) {
			Component t = defs.get(i).title();
			int cx = SlotsMenu.slotX(i, defs.size()) + 8;
			g.text(font, t, cx - font.width(t) / 2, 8, 0xFF404040, false);
		}
		g.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xFF404040, false);
	}
}
