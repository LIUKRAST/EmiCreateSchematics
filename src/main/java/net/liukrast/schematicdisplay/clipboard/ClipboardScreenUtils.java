package net.liukrast.schematicdisplay.clipboard;

import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.runtime.EmiFavorites;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public final class ClipboardScreenUtils {
    public static final String REAL_COUNT_KEY = "SchematicDisplay_RealCount";
    private ClipboardScreenUtils() {}

    public static boolean load(List<List<ClipboardEntry>> pages, final boolean save) {
        for(var page : pages) {
            for(var entry : page) {
                if(save) {
                    ItemStack stack1 = entry.icon.copy();
                    stack1.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, existingData ->
                            existingData.update(tag -> tag.putLong(REAL_COUNT_KEY, entry.itemAmount))
                    );
                    EmiStack emiStack = EmiStack.of(stack1);
                    emiStack.setAmount(1);
                    EmiFavorites.addFavorite(emiStack);
                }
            }
        }
        return true;
    }
}