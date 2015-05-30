/**
 * This class was created by BrassGoggledCoders modding team.
 * This class is available as part of the EnderGloves Mod for Minecraft.
 *
 * EnderGloves is open-source and is distributed under the MMPL v1.0 License.
 * (http://www.mod-buildcraft.com/MMPL-1.0.txt)
 *
 */
package brassutils.common;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;

import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;

import brassutils.client.gui.GuiHandler;
import brassutils.common.commands.CommandDeathNote;
import brassutils.common.commands.CommandFeed;
import brassutils.common.commands.CommandHeal;
import brassutils.common.commands.CommandHome;
import brassutils.common.commands.CommandInfo;
import brassutils.common.commands.CommandSayCoords;
import brassutils.common.config.Config;
import brassutils.common.gen.EnderGloveWorldGenerator;
import brassutils.common.lib.CreativeTabBrassUtils;
import brassutils.common.lib.EventHandlerEntity;
import brassutils.common.lib.EventHandlerWorld;
import brassutils.common.lib.LibInfo;

/**
 * @author Surseance
 *
 */
@Mod(modid = LibInfo.ID, name = LibInfo.NAME, version = LibInfo.VERSION, dependencies = "required-after:boilerplate")
public class BrassUtils
{
	@SidedProxy(clientSide = LibInfo.CLIENT_PROXY, serverSide = LibInfo.COMMON_PROXY)
	public static CommonProxy proxy;

	@Instance(LibInfo.ID)
	public static BrassUtils instance;

	public static CreativeTabs tabBU = new CreativeTabBrassUtils(CreativeTabs.getNextID(), LibInfo.ID);

	public EnderGloveWorldGenerator worldGen;
	public EventHandlerEntity entityEventHandler;
	public EventHandlerWorld worldEventHandler;
	public File directory;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		// Dirty, but it works
		if (Loader.isModLoaded("EnderGlove"))
			throw new RuntimeException(
					"Please uninstall EnderGlove to continue. EnderGlove has been merged into this mod, and so cannot be used alongside it. Old EnderGlove items will be transferred safely");
		if (Loader.isModLoaded("TurfMod"))
			throw new RuntimeException(
					"Please uninstall TurfMod to continue. TurfMod has been merged into this mod, and so cannot be used alongside it. Old TurfMod items will be transferred safely");
		event.getModMetadata().version = LibInfo.VERSION;
		this.directory = event.getModConfigurationDirectory();

		Config.initialize(event.getSuggestedConfigurationFile());

		this.entityEventHandler = new EventHandlerEntity();
		this.worldEventHandler = new EventHandlerWorld();

		FMLCommonHandler.instance().bus().register(this.entityEventHandler);
		MinecraftForge.EVENT_BUS.register(this.worldEventHandler);

		if (Config.totemGen)
			GameRegistry.registerWorldGenerator(this.worldGen = new EnderGloveWorldGenerator(), 100);

		Config.save();

		InitBlocks.init();
		InitItems.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerDisplayInformation();
		InitEntities.init();

		if (Config.recipeEnderGlove)
		{
			GameRegistry.addRecipe(new ItemStack(InitItems.itemEnderGlove), new Object[] { "EEE", "LNL", "LLL", 'L', Items.leather, 'N',
					Items.nether_star, 'E', Items.ender_eye });
		}
		if (Config.recipeEnderPocket)
		{
			GameRegistry.addRecipe(new ItemStack(InitItems.itemEnderPocket), new Object[] { "LXL", "XYX", "LXL", 'X', Blocks.obsidian, 'Y',
					Blocks.ender_chest, 'L', Items.leather });
		}
		GameRegistry.addRecipe(new ItemStack(InitBlocks.blockLeafCover, 3, 0), new Object[] { "LL", 'L', new ItemStack(Blocks.leaves, 1, 0) });
		GameRegistry.addRecipe(new ItemStack(InitBlocks.blockLeafCover, 3, 1), new Object[] { "LL", 'L', new ItemStack(Blocks.leaves, 1, 1) });
		GameRegistry.addRecipe(new ItemStack(InitBlocks.blockLeafCover, 3, 2), new Object[] { "LL", 'L', new ItemStack(Blocks.leaves, 1, 2) });
		GameRegistry.addRecipe(new ItemStack(InitBlocks.blockLeafCover, 3, 3), new Object[] { "LL", 'L', new ItemStack(Blocks.leaves, 1, 3) });
		GameRegistry.addRecipe(new ItemStack(InitBlocks.blockLeafCover, 3, 4), new Object[] { "LL", 'L', new ItemStack(Blocks.leaves2, 1, 0) });
		GameRegistry.addRecipe(new ItemStack(InitBlocks.blockLeafCover, 3, 5), new Object[] { "LL", 'L', new ItemStack(Blocks.leaves2, 1, 1) });
		GameRegistry.addRecipe(new ItemStack(Blocks.grass), new Object[] { "T", "D", 'T', InitBlocks.blockTurf, 'D', Blocks.dirt });
		GameRegistry.addShapelessRecipe(new ItemStack(InitItems.itemTurfKnife), new Object[] { Items.iron_sword });
		for (int i = 0; i < 6; i++)
			GameRegistry.addRecipe(new ItemStack(InitBlocks.blockLeafCover, 1, i + 6), new Object[] { "S", "L", 'S', Blocks.sand, 'L',
					new ItemStack(InitBlocks.blockLeafCover, 1, i) });

		if (Config.chestGen)
		{
			ChestGenHooks
					.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(InitItems.itemEnderGlove), 1, 1, 1));
			ChestGenHooks
					.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(new ItemStack(InitItems.itemEnderGlove), 1, 1, 1));
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(InitItems.itemEnderGlove), 1, 1, 2));
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	// Remap old items from merged in mods
	@EventHandler
	public void missingMapping(FMLMissingMappingsEvent event)
	{
		for (MissingMapping m : event.getAll())
		{
			if (m.type == Type.BLOCK)
			{
				if (m.name.equals("EnderGlove:BlockEnderTotem"))
					m.remap(GameRegistry.findBlock(LibInfo.ID, "BlockEnderTotem"));
				else if (m.name.equals("EnderGlove:blockTotemTop"))
					m.remap(GameRegistry.findBlock(LibInfo.ID, "BlockEnderTotemTop"));
				else if (m.name.equals("TurfMod:BlockTurf"))
					m.remap(GameRegistry.findBlock(LibInfo.ID, "BlockTurf"));
				else if (m.name.equals("TurfMod:BlockLeafCover"))
					m.remap(GameRegistry.findBlock(LibInfo.ID, "BlockLeafCover"));
				else if (m.name.equals("TurfMod:BlockGrassCover"))
					m.remap(GameRegistry.findBlock(LibInfo.ID, "BlockGrassCover"));
			}
			else if (m.type == Type.ITEM)
			{
				if (m.name.equals("EnderGlove:ItemEnderGlove"))
					m.remap(GameRegistry.findItem(LibInfo.ID, "ItemEnderGlove"));
				else if (m.name.equals("EnderGlove:ItemEnderPocket"))
					m.remap(GameRegistry.findItem(LibInfo.ID, "ItemEnderPocket"));
				else if (m.name.equals("EnderGlove:BlockEnderTotem"))
					m.remap(Item.getItemFromBlock(GameRegistry.findBlock(LibInfo.ID, "BlockEnderTotem")));
				else if (m.name.equals("EnderGlove:blockTotemTop"))
					m.remap(Item.getItemFromBlock(GameRegistry.findBlock(LibInfo.ID, "BlockEnderTotemTop")));
				else if (m.name.equals("TurfMod:ItemTurfKnife"))
					m.remap(GameRegistry.findItem(LibInfo.ID, "ItemTurfKnife"));
				else if (m.name.equals("TurfMod:BlockTurf"))
					m.remap(Item.getItemFromBlock(GameRegistry.findBlock(LibInfo.ID, "BlockTurf")));
				else if (m.name.equals("TurfMod:BlockLeafCover"))
					m.remap(Item.getItemFromBlock(GameRegistry.findBlock(LibInfo.ID, "BlockLeafCover")));
				else if (m.name.equals("TurfMod:BlockGrassCover"))
					m.remap(Item.getItemFromBlock(GameRegistry.findBlock(LibInfo.ID, "BlockGrassCover")));
			}
		}
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandDeathNote());
		event.registerServerCommand(new CommandHeal());
		// event.registerServerCommand(new CommandCraft());
		event.registerServerCommand(new CommandHome());
		event.registerServerCommand(new CommandSayCoords());
		event.registerServerCommand(new CommandFeed());
		event.registerServerCommand(new CommandInfo());
		// event.registerServerCommand(new CommandViewInv());
	}
}
