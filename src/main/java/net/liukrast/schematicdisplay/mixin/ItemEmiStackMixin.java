package net.liukrast.schematicdisplay.mixin;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.liukrast.schematicdisplay.clipboard.ClipboardScreenUtils.COUNT_TAG_KEY;

@SuppressWarnings("all")
@Mixin(ItemEmiStack.class)
public final class ItemEmiStackMixin {
    @Inject(at = @At("TAIL"), method = "render", remap = false)
    private void render(final GuiGraphics matrices,
                        final int x, final int y,
                        final float delta,
                        final int flags,
                        final CallbackInfo ci) {
        final EmiDrawContext context = EmiDrawContext.wrap(matrices);
        ItemEmiStack emiStack = (ItemEmiStack)(Object)this;
        ItemStack underlyingStack = emiStack.getItemStack();
        boolean shouldRenderCount = false;

        if (underlyingStack != null && underlyingStack.has(DataComponents.CUSTOM_DATA)) {
            CustomData customData = underlyingStack.get(DataComponents.CUSTOM_DATA);
            if (customData != null && customData.contains(COUNT_TAG_KEY)) {
                CompoundTag tag = customData.copyTag();
                if (tag.getBoolean(COUNT_TAG_KEY))
                    shouldRenderCount = true;
            }
        }
        if ((flags) != 0 && shouldRenderCount) {
            final StringBuilder bob = new StringBuilder();
            if (((ItemEmiStack)(Object)this).getAmount() != 1)
                bob.append(((ItemEmiStack)(Object)this).getAmount());
            EmiRenderHelper.renderAmount(context, x, y, EmiPort.literal(bob.toString()));
        }
    }
}