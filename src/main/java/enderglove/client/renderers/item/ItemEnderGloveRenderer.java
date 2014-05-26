/**
 * This class was created by <Surseance> as a part of the
 * EnderGlove mod for Minecraft.
 *
 * This mod is registered under the WTFPL v2.0. Please read the
 * COPYING.WTFPL file for more details.
 *
 * File created @[May 14, 2014, 9:06:20 PM]
 */
package enderglove.client.renderers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import enderglove.common.lib.InventoryHelper;
import enderglove.common.lib.LibInfo;

/**
 * The ItemRenderer for the ender glove item.
 *
 * @author Surseance (Johnny Eatmon) <jmaeatmon@gmail.com>
 *
 */
public class ItemEnderGloveRenderer implements IItemRenderer
{
	@SuppressWarnings("unused")
	private final ModelEnderGlove modelGlove = new ModelEnderGlove();
	@SuppressWarnings("unused")
	private final ResourceLocation gloveTex = new ResourceLocation(
			LibInfo.PREFIX.replace(":", ""), "textures/models/modelglove.png");

	@Override
	public boolean handleRenderType(final ItemStack item,
			final ItemRenderType type)
	{
		switch (type)
		{
		case EQUIPPED_FIRST_PERSON:
			return true;
		case EQUIPPED:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(final ItemRenderType type,
			final ItemStack item, final ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item,
			final Object... data)
	{
		switch (type)
		{
		case EQUIPPED_FIRST_PERSON:
		{
			final Minecraft mc = Minecraft.getMinecraft();

			if (mc.thePlayer.inventory.hasItem(Items.ender_eye))
			{
				GL11.glPushMatrix();
				final int slot = InventoryHelper.isInPlayerInventory(
						Minecraft.getMinecraft().thePlayer, Items.ender_eye);
				final ItemStack is = Minecraft.getMinecraft().thePlayer.inventory
						.getStackInSlot(slot);

				renderEnderEye(item, is);
				// Minecraft.getMinecraft().renderEngine.bindTexture(gloveTex);
				// this.modelGlove.render((Entity)data[1], 0.0F, 0.0F, 0.0F,
				// 0.0F, 0.0F, 0.0625F);
				GL11.glPopMatrix();
			}
		}
		default:
			break;
		}
	}

	private void renderEnderEye(final ItemStack item, final ItemStack is)
	{
		final Minecraft mc = Minecraft.getMinecraft();

		GL11.glTranslatef(8, 0, 0);
		final float scale = 1.9F;
		GL11.glScalef(scale, scale, scale);
		final float angle = mc.theWorld.getWorldTime() * 11.6F;
		GL11.glRotatef(angle, 0 - 0.5F, 0 - 0.5F, 0);

		if (is != null)
		{
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
			int renderPass = 0;

			do
			{
				final IIcon icon = Items.ender_eye.getIcon(item, renderPass);

				if (icon != null)
				{
					final float minU = icon.getMinU();
					final float maxU = icon.getMaxU();
					final float minV = icon.getMinV();
					final float maxV = icon.getMaxV();
					ItemRenderer.renderItemIn2D(Tessellator.instance, maxU,
							minV, minU, maxV, icon.getIconWidth(),
							icon.getIconHeight(), 1.0F / 16.0F);
					GL11.glColor3f(1.0F, 1.0F, 1.0F);
				}

				renderPass++;
			} while (renderPass < is.getItem().getRenderPasses(
					is.getItemDamage()));
		}
	}
}