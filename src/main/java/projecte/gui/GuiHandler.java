package projecte.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import projecte.container.ContainerEnergyCollectorMK1;
import projecte.container.ContainerRelayMK1;
import projecte.tile.TileEnergyCollectorMK1;
import projecte.tile.TileRelayMK1;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);

		if (entity instanceof TileEnergyCollectorMK1) {
			return new ContainerEnergyCollectorMK1(player.inventory, (TileEnergyCollectorMK1) entity);
		}

		if (entity instanceof TileRelayMK1) {
			return new ContainerRelayMK1(player.inventory, (TileRelayMK1) entity);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);

		if (entity instanceof TileEnergyCollectorMK1) {
			return new GuiEnergyCollectorMK1(player.inventory, (TileEnergyCollectorMK1) entity);
		}

		if (entity instanceof TileRelayMK1) {
			return new GuiRelayMK1(player.inventory, (TileRelayMK1) entity);
		}
		return null;
	}

}
