package net.frozenblock.liukrast.schematicdisplay.clipboard;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.runtime.EmiFavorites;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ClipboardScreenUtils {
    private ClipboardScreenUtils() {}

    public static boolean load(@Nullable final CompoundTag tag, final boolean save) {
        try {
            if (tag == null) throw new Exception("Tag is null");

            final ListTag pages = tag.getList("Pages", Tag.TAG_COMPOUND);
            if (pages.isEmpty()) throw new Exception("Pages size is 0");

            for (int i = 0; i < pages.size(); i++) {
                final ListTag entries = pages.getCompound(i).getList("Entries", Tag.TAG_COMPOUND);
                if (entries.isEmpty()) throw new Exception("Entries size is 0");

                for (int k = 0; k < entries.size(); k++) {
                    final CompoundTag entry = entries.getCompound(k);
                    final CompoundTag icon = entry.getCompound("Icon");
                    final String id = icon.getString("id");
                    final ResourceLocation location = new ResourceLocation(id);
                    final String text = entry.getString("Text");
                    final Component lT = Component.Serializer.fromJson(text);

                    if (lT == null) return false;
                    final List<Component> components = lT.getSiblings();

                    if (components.isEmpty()) continue;
                    final int c = Integer.parseInt(components.get(1)
                                        .toString()
                                        .replace("literal{\n x", "")
                                        .replace("}[style={color=black}]", ""));
                    if (save) {
                        final ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getDelegateOrThrow(location));
                        final EmiIngredient ingredient = EmiIngredient.of(Ingredient.of(stack));
                        ingredient.setAmount(c);
                        EmiFavorites.addFavorite(ingredient);
                    }
                }
            }
            return true;
        } catch (final Exception ignore) {
            return false;
        }
    }
}
