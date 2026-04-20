
package com.blue_gamerwolf.sculkvoid.modded.init;

import com.blue_gamerwolf.sculkvoid.modded.enchantment.Clanking;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.OrphanObliterator;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.curses.FowlSmell;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.curses.customerservice;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.EnvyEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.GreedEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.GluttonyEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.LustEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.PrideEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.SlothEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.WrathEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.unbreakable.UnbreakableEnchantment;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import com.blue_gamerwolf.sculkvoid.modded.enchantment.*;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

public class SculkVoidModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SculkVoidMod.MODID);
	public static final RegistryObject<Enchantment> SCULKNESS = REGISTRY.register("sculkness", () -> new SculknessEnchantment());
	public static final RegistryObject<Enchantment> VOID_GOD = REGISTRY.register("void_god", () -> new VoidGodEnchantment());
	public static final RegistryObject<Enchantment> SCULK_GOD = REGISTRY.register("sculk_god", () -> new SculkGodEnchantment());
    public static final RegistryObject<Enchantment> UNBREAKABLE = REGISTRY.register("unbreakable", () -> new UnbreakableEnchantment());
    public static final RegistryObject<Enchantment> FOWL_SMELL = REGISTRY.register("fowl_smell", () -> new FowlSmell());
    public static final RegistryObject<Enchantment> CURSE_OF_CUSTOMER_SERVICE = REGISTRY.register("curse_of_customer_service", customerservice::new);
    public static final RegistryObject<Enchantment> ORPHAN_OBLITERATOR = REGISTRY.register("orphan_obliterator", OrphanObliterator::new);
    public static final RegistryObject<Enchantment> ENVY = REGISTRY.register("envy", EnvyEnchantment::new);
    public static final RegistryObject<Enchantment> GREED = REGISTRY.register("greed", GreedEnchantment::new);
    public static final RegistryObject<Enchantment> GLUTTONY = REGISTRY.register("gluttony", () -> new GluttonyEnchantment());
    public static final RegistryObject<Enchantment> LUST = REGISTRY.register("lust", LustEnchantment::new);
    public static final RegistryObject<Enchantment> PRIDE = REGISTRY.register("pride", PrideEnchantment::new);
    public static final RegistryObject<Enchantment> SLOTH = REGISTRY.register("sloth", SlothEnchantment::new);
    public static final RegistryObject<Enchantment> WRATH = REGISTRY.register("wrath", WrathEnchantment::new);
    public static final RegistryObject<Enchantment> CLANKING = REGISTRY.register("clanking", () -> new Clanking());
}
