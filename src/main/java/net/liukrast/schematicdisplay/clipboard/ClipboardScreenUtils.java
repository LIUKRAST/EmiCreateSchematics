package net.liukrast.schematicdisplay.clipboard;

import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.runtime.EmiFavorites;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public final class ClipboardScreenUtils {
    private ClipboardScreenUtils() {}

    public static boolean load(List<List<ClipboardEntry>> pages, final boolean save) {
        for(var page : pages) {
            for(var entry : page) {
                if(save) {
                    var stack1 = entry.icon.copy();
                    final EmiIngredient ingredient = EmiIngredient.of(Ingredient.of(stack1));
                    ingredient.setAmount(entry.itemAmount);
                    EmiFavorites.addFavorite(ingredient);
                }
            }
        }
        return true;
    }
}