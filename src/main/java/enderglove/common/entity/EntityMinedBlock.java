/**
 * This class was created by <Surseance> as a part of the
 * EnderGloves mod for Minecraft. 
 *
 * This mod is registered under the WTFPL v2.0. Please read the
 * COPYING.WTFPL file for more details.
 *
 * File created @[May 25, 2014, 3:05:32 PM] 
 */
package enderglove.common.entity;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Surseance (Johnny Eatmon)
 * Email: surseance@autistici.org
 *
 */
public class EntityMinedBlock extends Entity
{
	public Block block;
	public int metadata;
	public NBTTagCompound tagCompound;

	public static float scale = 0.9F;

	public EntityMinedBlock(World world)
	{
		super(world);
	}

	public EntityMinedBlock(World world, double posX, double posY, double posZ, Block block)
	{
		this(world, posX, posY, posZ, block, 0);
	}

	public EntityMinedBlock(World world, double posX, double posY, double posZ, Block block, int md)
	{
		super(world);
		this.block = block;
		this.metadata = md;
		//this.preventEntitySpawning = true; 
		//this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(posX, posY, posZ);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	protected void entityInit() {}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public void onUpdate()
	{
		if ((this.worldObj.getWorldTime() % 1) == 0)
		{
			this.scale -= 0.0625F;			
		}
		
		if (this.scale <= 0)
		{
			this.scale = 0.9F;
			this.setDead();
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setByte("Tile", (byte)Block.getIdFromBlock(this.block));
		tagCompound.setInteger("TileID", Block.getIdFromBlock(this.block));
		tagCompound.setByte("Data", (byte)this.metadata);

		if (this.tagCompound != null)
		{
			tagCompound.setTag("TileEntityData", this.tagCompound);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompound)
	{
		if (tagCompound.hasKey("TileID", 99))
		{
			this.block = Block.getBlockById(tagCompound.getInteger("TileID"));
		}
		else
		{
			this.block = Block.getBlockById(tagCompound.getByte("Tile") & 255);
		}

		this.metadata = tagCompound.getByte("Data") & 255;

		if (tagCompound.hasKey("TileEntityData", 10))
		{
			this.tagCompound = tagCompound.getCompoundTag("TileEntityData");
		}
	}

	@Override
	public void addEntityCrashInfo(CrashReportCategory crc)
	{
		super.addEntityCrashInfo(crc);
		crc.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(this.block)));
		crc.addCrashSection("Immitating block data", Integer.valueOf(this.metadata));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getShadowSize()
	{
		return 0.0F;
	}

	@SideOnly(Side.CLIENT)
	public World getWorldObj()
	{
		return this.worldObj;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	public Block getBlock()
	{
		return this.block;
	}
}
