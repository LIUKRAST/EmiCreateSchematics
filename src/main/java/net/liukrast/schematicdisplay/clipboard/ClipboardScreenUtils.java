package net.liukrast.schematicdisplay.clipboard;

import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.runtime.EmiFavorite;
import dev.emi.emi.runtime.EmiFavorites;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ClipboardScreenUtils {
    public static final String REAL_COUNT_KEY = "CreateEMISchematicsData";
    private ClipboardScreenUtils() {}

    public static void load(List<List<ClipboardEntry>> pages) {
        List<ClipboardEntry> allEntries = new ArrayList<>();
        for(var page : pages)
            allEntries.addAll(page);
        allEntries.sort(Comparator.comparingInt((ClipboardEntry entry) -> entry.itemAmount).reversed());

        var copiedList = new ArrayList<>(EmiFavorites.favorites);

        copiedList.stream()
                .filter(fav -> {
                    if(!(fav.getStack() instanceof EmiStack stack)) return false;
                    var data = stack.getItemStack().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

                    if(data.isEmpty()) return false;
                    return data.contains(REAL_COUNT_KEY);
                })
                .forEach(EmiFavorites::removeFavorite);

        for(var entry : allEntries) {
            ItemStack stack1 = entry.icon.copy();
            stack1.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, existingData ->
                    existingData.update(tag -> tag.putLong(REAL_COUNT_KEY, entry.itemAmount))
            );
            EmiStack emiStack = EmiStack.of(stack1);
            emiStack.setAmount(1);



            if(!entry.checked) EmiFavorites.addFavorite(emiStack);
        }
    }
}