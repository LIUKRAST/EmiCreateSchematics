package net.liukrast.schematicdisplay.clipboard;

import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.runtime.EmiFavorites;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public final class ClipboardScreenUtils {
    public static final String COUNT_TAG_KEY = "CreateEmiSchem_ShowCount";
    private ClipboardScreenUtils() {}

    public static boolean load(List<List<ClipboardEntry>> pages, final boolean save) {
        for(var page : pages) {
            for(var entry : page) {
                if(save) {
                    ItemStack stack1 = entry.icon.copy();
                    stack1.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, existingData ->
                            existingData.update(tag -> tag.putBoolean(COUNT_TAG_KEY, true))
                    );
                    final EmiIngredient ingredient = EmiIngredient.of(Ingredient.of(stack1));
                    ingredient.setAmount(entry.itemAmount);
                    EmiFavorites.addFavorite(ingredient);
                }
            }
        }
        return true;
    }
}