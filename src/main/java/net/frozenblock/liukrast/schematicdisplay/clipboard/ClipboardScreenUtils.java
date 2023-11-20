package net.frozenblock.liukrast.schematicdisplay.clipboard;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.runtime.EmiFavorites;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClipboardScreenUtils {
    public static boolean load(@Nullable CompoundTag tag, boolean save) {
        try {
            if(tag == null) throw new Exception("Tag is null");
            ListTag pages = tag.getList("Pages", Tag.TAG_COMPOUND);
            if(pages.size() == 0) throw new Exception("Pages size is 0");
            for (int i = 0; i < pages.size(); i++) {
                ListTag entries = pages.getCompound(i).getList("Entries", Tag.TAG_COMPOUND);
                if(entries.size() == 0) throw new Exception("Entries size is 0");
                for (int k = 0; k < entries.size(); k++) {
                    CompoundTag entry = entries.getCompound(k);
                    CompoundTag icon = entry.getCompound("Icon");
                    String id = icon.getString("id");
                    ResourceLocation location = new ResourceLocation(id);
                    String text = entry.getString("Text");
                    Component lT = Component.Serializer.fromJson(text);
                    List<Component> components = lT.getSiblings();
                    if(components.size() == 0) continue;
                    int c = Integer.parseInt(components.get(1).toString().replace("literal{\n x", "").replace("}[style={color=black}]", ""));
                    if (save) {
                        ItemStack stack = new ItemStack(Registry.ITEM.get(location));
                        EmiIngredient ingredient = EmiIngredient.of(Ingredient.of(stack));
                        ingredient.setAmount(c);
                        EmiFavorites.addFavorite(ingredient);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }
}
