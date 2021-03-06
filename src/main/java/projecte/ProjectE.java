package projecte;

import codechicken.nei.guihook.GuiContainerManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import projecte.api.emc.EmcRegistry;
import projecte.crafting.PhilosopherStoneCraftingHandler;
import projecte.event.CraftingEvent;
import projecte.event.EventCloakRenderer;
import projecte.event.VolcaniteTossEvent;
import projecte.gui.GuiHandler;
import projecte.handlers.FurnaceFuelHandler;
import projecte.handlers.TooltipHandler;
import projecte.handlers.TooltipHandlerNEI;
import projecte.packet.PacketManager;
import projecte.proxy.CommonProxy;
import projecte.util.CreativeTab;
import projecte.util.OredictUtil;

import java.util.logging.Logger;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, useMetadata = false)
public class ProjectE {
	
	public static CreativeTabs tab = new CreativeTab(ModInfo.MOD_ID);

	@Instance(ModInfo.MOD_ID)
	public static ProjectE inst;

	@SidedProxy(clientSide = "projecte.proxy.ClientProxy", serverSide = "projecte.proxy.CommonProxy", modId = ModInfo.MOD_ID)
	public static CommonProxy proxy;
	
	public static Logger log = Logger.getLogger(ModInfo.MOD_NAME);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		/* Register vanilla oredict names */
		OredictUtil.registerVanillaOredict();
		PacketManager.init();
		/* Register items and blocks and fluids */
		proxy.registerFluids();
		proxy.registerBlocks();
		proxy.registerItems();
		proxy.registerRenders();
		/* Register default EMC values */
		EmcRegistry.registerDefault();

		/* Register channels */
		PacketManager.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		/* Register events */
		FMLCommonHandler.instance().bus().register(new CraftingEvent());

		/* Register handlers */
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()){

			MinecraftForge.EVENT_BUS.register(new TooltipHandler());
			EventCloakRenderer.addCapes();

            //If NEI is loaded, it will use NEI's system instead
            if(Loader.isModLoaded("NotEnoughItems"))
                GuiContainerManager.addTooltipHandler(new TooltipHandlerNEI());
            else
                FMLCommonHandler.instance().bus().register(new TooltipHandler());

            /*
			 * Disabled due to a different event
			 * MinecraftForge.EVENT_BUS.register(new TooltipHandler());
			 */
			EventCloakRenderer.addCapes();


		}
		MinecraftForge.EVENT_BUS.register(new VolcaniteTossEvent());
		//FMLCommonHandler.instance().bus().register(new VolcaniteTossEvent());
	//	MinecraftForge.EVENT_BUS.register(new KeyHandler());
		
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		GameRegistry.addRecipe(PhilosopherStoneCraftingHandler.inst);
		
		/* Register recipes */
		proxy.addRecipes();
		proxy.registerTiles();
		/* Register GUI handler */
		NetworkRegistry.INSTANCE.registerGuiHandler(inst, new GuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		EmcRegistry.onReachPostInit();
	}

	@EventHandler
	public void onFinishLoading(FMLLoadCompleteEvent event) {
		// FIXME generate recipes
	}

}
