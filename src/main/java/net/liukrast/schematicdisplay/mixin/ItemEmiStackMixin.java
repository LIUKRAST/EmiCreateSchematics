package net.liukrast.schematicdisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import net.liukrast.schematicdisplay.EMICreateSchematics;
import net.liukrast.schematicdisplay.clipboard.ClipboardScreenUtils;
import net.liukrast.schematicdisplay.util.SchematicDisplayUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings("all")
@Mixin(ItemEmiStack.class)
public final class ItemEmiStackMixin {
    @Inject(at = @At("TAIL"), method = "render", remap = false)
    private void render(final GuiGraphics matrices,
                        final int x, final int y,
                        final float delta,
                        final int flags,
                        final CallbackInfo ci) {
        if (flags == 0) return;
        ItemEmiStack emiStack = (ItemEmiStack)(Object)this;
        ItemStack underlyingStack = emiStack.getItemStack();
        long customAmount = getRealCount(underlyingStack);

        if (customAmount > 1) {
            String text = SchematicDisplayUtils.abbreviate(customAmount);
            Font font = Minecraft.getInstance().font;
            int textWidth = font.width(text);
            float availableWidth = 15.0f;
            float scale = Math.min(1.0f, availableWidth / (float) textWidth);

            PoseStack pose = matrices.pose();
            pose.pushPose();
            pose.translate(x, y, 200);
            pose.scale(scale, scale, 1.0f);

            float targetRight = 17.0f;
            float targetBottom = 16.0f + scale;
            float drawX = (targetRight / scale) - textWidth;
            float drawY = (targetBottom / scale) - font.lineHeight;

            matrices.drawString(font, text, (int)drawX, (int)drawY, 0xFFFFFF, true);
            pose.popPose();
        }
    }

    @Inject(at = @At("RETURN"), method = "getTooltip", remap = false)
    private void getTooltip(CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        List<ClientTooltipComponent> list = cir.getReturnValue();
        if (list == null) return;
        ItemEmiStack emiStack = (ItemEmiStack)(Object)this;
        ItemStack underlyingStack = emiStack.getItemStack();
        long customAmount = getRealCount(underlyingStack);

        if (customAmount > 1) {
            String formattedCount = String.format("%,d", customAmount);
            Component textComponent = Component.translatable("gui." + EMICreateSchematics.MOD_ID + ".tooltip.count", formattedCount)
                    .withStyle(ChatFormatting.GRAY);
            list.add(new ClientTextTooltip(textComponent.getVisualOrderText()));
        }
    }

    private long getRealCount(ItemStack stack) {
        if (stack != null && stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null && customData.contains(ClipboardScreenUtils.REAL_COUNT_KEY)) {
                CompoundTag tag = customData.copyTag();
                if (tag.contains(ClipboardScreenUtils.REAL_COUNT_KEY))
                    return tag.getLong(ClipboardScreenUtils.REAL_COUNT_KEY);
            }
        }
        return -1;
    }
}