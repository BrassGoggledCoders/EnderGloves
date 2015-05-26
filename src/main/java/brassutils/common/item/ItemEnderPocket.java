/**
 * This class was created by BrassGoggledCoders modding team.
 * This class is available as part of the EnderGloves Mod for Minecraft.
 *
 * EnderGloves is open-source and is distributed under the MMPL v1.0 License.
 * (http://www.mod-buildcraft.com/MMPL-1.0.txt)
 *
 */
package brassutils.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import boilerplate.common.baseclasses.RootItem;
import brassutils.common.BrassUtils;
import brassutils.common.lib.LibInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Surseance
 *
 */
public class ItemEnderPocket extends RootItem
{
	public ItemEnderPocket()
	{
		super();
		this.setFull3D();
		this.setCreativeTab(BrassUtils.tabBU);
		this.setNoRepair();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ir)
	{
		this.itemIcon = ir.registerIcon(LibInfo.PREFIX + "enderpocket");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.openGui(BrassUtils.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return stack;
	}

	@SuppressWarnings("all")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag)
	{
		list.add(EnumChatFormatting.GREEN + "Shows a random");
		list.add(EnumChatFormatting.GREEN + "six slots from your ender chest");
	}

	@Override
	public EnumRarity getRarity(ItemStack is)
	{
		return EnumRarity.rare;
	}
}