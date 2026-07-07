package net.liukrast.schematicdisplay.clipboard;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.liukrast.schematicdisplay.EMICreateSchematics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class ClipboardTreeRecipe extends BasicEmiRecipe {
    private static final int DISPLAYED_INPUTS = 9;
    private final Component overflowLabel;

    public ClipboardTreeRecipe(List<EmiStack> inputs, EmiStack output, ResourceLocation id) {
        super(VanillaEmiRecipeCategories.CRAFTING, id, 118, 72);
        this.inputs.addAll(inputs);
        this.outputs.add(output);

        int hiddenInputs = Math.max(0, inputs.size() - DISPLAYED_INPUTS);
        overflowLabel = hiddenInputs > 0
                ? Component.translatable("gui." + EMICreateSchematics.MOD_ID + ".clipboard.tree_more", hiddenInputs)
                : Component.empty();
    }

    public static boolean isVirtual(EmiRecipe recipe) {
        return recipe instanceof ClipboardTreeRecipe;
    }

    @Override
    public boolean hideCraftable() {
        return true;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addText(
                Component.translatable("gui." + EMICreateSchematics.MOD_ID + ".clipboard.tree_recipe"),
                0,
                0,
                0x404040,
                false
        );
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 22);

        for (int i = 0; i < DISPLAYED_INPUTS; i++) {
            if (i < inputs.size()) {
                widgets.addSlot(inputs.get(i), i % 3 * 18, 12 + i / 3 * 18);
            } else {
                widgets.addSlot(i % 3 * 18, 12 + i / 3 * 18);
            }
        }

        widgets.addSlot(outputs.get(0), 92, 18).large(true).recipeContext(this);
        if (!overflowLabel.getString().isEmpty()) {
            widgets.addText(overflowLabel, 0, 62, 0x787878, false);
        }
    }
}
