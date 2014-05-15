/**
 * This class was created by <Surseance> as a part of the
 * EnderGloves mod for Minecraft. 
 *
 * This mod is registered under the WTFPL v2.0. Please read the
 * COPYING.WTFPL file for more details.
 *
 * File created @[May 14, 2014, 8:10:36 PM] 
 */
package endergloves.common.config;

import java.io.File;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import endergloves.common.lib.enchantment.EnchantmentAffluency;
import endergloves.common.lib.enchantment.EnchantmentArtisan;
import endergloves.common.lib.enchantment.EnchantmentCreative;
import endergloves.common.lib.enchantment.EnchantmentSpelunker;
import endergloves.common.lib.enchantment.EnchantmentTeleport;

/**
 * @author Surseance (Johnny Eatmon)
 * <jmaeatmon@gmail.com>
 *
 */
public class Config
{
	public static Configuration config;
	
	public static Enchantment enchAffluency = null;
	public static Enchantment enchArtisan = null;
	public static Enchantment enchSpelunker = null;
	public static Enchantment enchTeleport = null;
	public static Enchantment enchCreative = null;
	public static Enchantment enchFlameTouch = null;
	
	public static int enchAffluencyId;
	public static int enchArtisanId;
	public static int enchSpelunkerId;
	public static int enchTeleportId;
	public static int enchCreativeId;
	public static int enchFlameTouchId;
	
	public static void initialize(File file)
	{
		config = new Configuration(file);
		config.addCustomCategoryComment("Enchantments", "EnderGlove enchantments");
		config.load();
		
		int enchIndex = 160;
	
		Property enchAff = config.get("Enchantments", "ench_affluency", enchIndex++);
		enchAffluency = new EnchantmentAffluency(enchAff.getInt(), 4);
		enchAffluencyId = enchAff.getInt();
		Enchantment.addToBookList(enchAffluency);
		
		Property enchArt = config.get("Enchantments", "ench_artisan", enchIndex++);
		enchArtisan = new EnchantmentArtisan(enchArt.getInt(), 4);
		enchArtisanId = enchAff.getInt();
		Enchantment.addToBookList(enchAffluency);
		
		Property enchSpe = config.get("Enchantments", "ench_affluency", enchIndex++);
		enchSpelunker = new EnchantmentSpelunker(enchSpe.getInt(), 4);
		enchSpelunkerId = enchAff.getInt();
		Enchantment.addToBookList(enchAffluency);
		
		Property enchTel = config.get("Enchantments", "ench_affluency", enchIndex++);
		enchTeleport = new EnchantmentTeleport(enchTel.getInt(), 4);
		enchTeleportId = enchAff.getInt();
		Enchantment.addToBookList(enchAffluency);
		
		Property enchOP = config.get("Enchantments", "ench_affluency", enchIndex++);
		enchCreative = new EnchantmentCreative(enchOP.getInt(), 4);
		enchCreativeId = enchAff.getInt();
		Enchantment.addToBookList(enchAffluency);
		
		config.save();
	}
	
	public static void save()
	{
		config.save();
	}
}