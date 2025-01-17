package xyz.amymialee.scarybees;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import xyz.amymialee.scarybees.cca.BeeMaskComponent;
import xyz.amymialee.scarybees.item.MaskItem;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScaryBees implements ModInitializer, EntityComponentInitializer {
    public static String MOD_ID = "scarybees";
    public static final TagKey<Item> BEE_MASKS = TagKey.of(Registries.ITEM.getKey(), id("bee_masks"));
    public static final TagKey<Item> SPAWNABLE_BEE_MASKS = TagKey.of(Registries.ITEM.getKey(), id("spawnable_bee_masks"));
    public static final ItemGroup BEE_GROUP = FabricItemGroup.builder().displayName(Text.translatable("itemGroup." + MOD_ID)).icon(ScaryBees::getIcon).build();
    public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    public static final Item MASK_BEE = createMask("mask_bee");
    public static final Item MASK_CREEPER = createMask("mask_creeper");
    public static final Item MASK_ENDERMAN = createMask("mask_enderman");
    public static final Item MASK_REDSTONE_GOLEM = createMask("mask_redstone_golem");
    public static final Item MASK_SKELETON = createMask("mask_skeleton");
    public static final Item MASK_SQUID = createMask("mask_squid");

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, id(MOD_ID), BEE_GROUP);
        ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
        Registries.ITEM_GROUP.getKey(BEE_GROUP).ifPresent(key -> ITEMS.keySet().forEach(stack -> ItemGroupEvents.modifyEntriesEvent(key).register(content -> content.add(stack))));
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(BeeEntity.class, BeeMaskComponent.KEY).respawnStrategy(RespawnCopyStrategy.INVENTORY).end(BeeMaskComponent::new);
    }

    public static @NotNull Item createMask(String name) {
        var item = new MaskItem(new Item.Settings().maxCount(1));
        ITEMS.put(item, id(name));
        return item;
    }

    public static @NotNull ItemStack getIcon() {
        return new ItemStack(MASK_CREEPER);
    }

    public static @NotNull Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }
}