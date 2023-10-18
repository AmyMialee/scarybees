package xyz.amymialee.scarybees;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
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
import xyz.amymialee.scarybees.cca.BeeMaskComponent;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScaryBees implements ModInitializer, EntityComponentInitializer {
    public static String MOD_ID = "scarybees";
    public static final ComponentKey<BeeMaskComponent> BEE_MASK = ComponentRegistry.getOrCreate(id("mask"), BeeMaskComponent.class);
    public static final TagKey<Item> BEE_MASKS = TagKey.of(Registries.ITEM.getKey(), id("bee_masks"));
    public static final ItemGroup BEE_GROUP = FabricItemGroup.builder().displayName(Text.translatable("itemGroup." + MOD_ID)).icon(ScaryBees::getIcon).build();
    public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    public static final Item CREEPER_MASK = createMask("creeper_mask");

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, id(MOD_ID), BEE_GROUP);
        ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
        Registries.ITEM_GROUP.getKey(BEE_GROUP).ifPresent(key -> ITEMS.keySet().forEach(stack -> ItemGroupEvents.modifyEntriesEvent(key).register(content -> content.add(stack))));
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(BeeEntity.class, BEE_MASK).respawnStrategy(RespawnCopyStrategy.INVENTORY).end(entity -> new BeeMaskComponent());
    }

    public static @NotNull Item createMask(String name) {
        var item = new Item(new FabricItemSettings().maxCount(1));
        ITEMS.put(item, id(name));
        return item;
    }

    public static ItemStack getIcon() {
        return new ItemStack(CREEPER_MASK);
    }

    public static @NotNull Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}