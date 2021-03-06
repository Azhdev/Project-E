package projecte.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import projecte.ModInfo;
import projecte.api.emc.EmcData;
import projecte.api.emc.EmcRegistry;
import projecte.container.ContainerCondenser;
import projecte.tile.TileCondenser;

public class GuiCondenser extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/energyCondenser.png");

	public TileCondenser tile;

	public GuiCondenser(InventoryPlayer inventoryPlayer, TileCondenser entity) {
		super(new ContainerCondenser(inventoryPlayer, entity));
		this.tile = entity;
		this.xSize = 256;
		this.ySize = 234;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		/*
		 * String name = StatCollector
		 * .translateToLocal("container.EnergyCollectMK1");
		 */

		// Not needed because we have the item and the player can shift hover to see the information
		// fontRendererObj.drawString(tile.getEmcStored() + " EMC", 175, 10, 4210752);

		// this.fontRendererObj.drawString(name, this.xSize / 2 -
		// this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		// this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"),
		// 8, this.ySize - 96, 4210752);

	}

	@Override
	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		double w = 103;
		
		ItemStack is = tile.getStackInSlot(tile.getSizeInventory() - 2);
		if(is != null){
			EmcData data = EmcRegistry.getValue(is);
			if(data != null){
				double max = w;
				w *= tile.getEmcStored();
				w /= data.getValue();
				w = Math.min(w, max);
			}else{
				w = 0;
			}
		}else{
			w = 0;
		}
		
		drawTexturedModalRect(guiLeft + 67, guiTop + 9, 0, 234, (int) w, 10);

	}
}
