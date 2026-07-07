package net.liukrast.schematicdisplay.clipboard;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.bom.BoM;
import net.liukrast.schematicdisplay.EMICreateSchematics;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ClipboardScreenUtils {
    public static final String REAL_COUNT_KEY = "CreateEMISchematicsData";
    public static final String VIRTUAL_RECIPE_KEY = "CreateEMISchematicsVirtualRecipe";
    private static long emi_create_schematics$recipeCounter = 0;

    private ClipboardScreenUtils() {}

    public static void load(List<List<ClipboardEntry>> pages) {
        List<EntryAmount> remainingEntries = collectRemainingEntries(pages);
        if (remainingEntries.isEmpty()) {
            notifyEmptyTree();
            return;
        }

        List<EmiStack> inputs = new ArrayList<>(remainingEntries.size());
        for (EntryAmount entry : remainingEntries) {
            inputs.add(EmiStack.of(entry.stack.copy()).setAmount(entry.amount));
        }

        ClipboardTreeRecipe recipe = new ClipboardTreeRecipe(
                inputs,
                createVirtualOutput(),
                nextVirtualRecipeId()
        );

        BoM.setGoal(recipe);
        BoM.craftingMode = true;

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(null);
        EmiApi.viewRecipeTree();
    }

    private static List<EntryAmount> collectRemainingEntries(List<List<ClipboardEntry>> pages) {
        List<EntryAmount> mergedEntries = new ArrayList<>();
        for (List<ClipboardEntry> page : pages) {
            for (ClipboardEntry entry : page) {
                if (entry.checked || entry.icon.isEmpty() || entry.itemAmount <= 0) {
                    continue;
                }
                mergeEntry(mergedEntries, entry.icon, entry.itemAmount);
            }
        }
        mergedEntries.sort(Comparator.comparingLong((EntryAmount entry) -> entry.amount).reversed());
        return mergedEntries;
    }

    private static void mergeEntry(List<EntryAmount> mergedEntries, ItemStack stack, long amount) {
        for (EntryAmount entry : mergedEntries) {
            if (ItemStack.isSameItemSameComponents(entry.stack, stack)) {
                entry.amount += amount;
                return;
            }
        }
        mergedEntries.add(new EntryAmount(stack.copy(), amount));
    }

    private static EmiStack createVirtualOutput() {
        ItemStack stack = AllBlocks.CLIPBOARD.asStack();
        stack.set(DataComponents.CUSTOM_NAME, Component.translatable("gui." + EMICreateSchematics.MOD_ID + ".clipboard.tree_output"));
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY,
                existingData -> existingData.update(tag -> tag.putBoolean(VIRTUAL_RECIPE_KEY, true)));
        return EmiStack.of(stack).comparison(Comparison.compareComponents());
    }

    private static ResourceLocation nextVirtualRecipeId() {
        return ResourceLocation.fromNamespaceAndPath(
                EMICreateSchematics.MOD_ID,
                "/clipboard/" + emi_create_schematics$recipeCounter++
        );
    }

    private static void notifyEmptyTree() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.displayClientMessage(
                    Component.translatable("gui." + EMICreateSchematics.MOD_ID + ".clipboard.tree_empty"),
                    true
            );
        }
    }

    private static final class EntryAmount {
        private final ItemStack stack;
        private long amount;

        private EntryAmount(ItemStack stack, long amount) {
            this.stack = stack;
            this.amount = amount;
        }
    }
}
