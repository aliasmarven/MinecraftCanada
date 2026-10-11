package xen42.canadamod.gui;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import xen42.canadamod.item.ThermosContentsComponent;

import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ThermosTooltipComponent implements ClientTooltipComponent {
    private static final Identifier BUNDLE_PROGRESS_BAR_BORDER_TEXTURE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_border");
    private static final Identifier BUNDLE_PROGRESS_BAR_FILL_TEXTURE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
    private static final Identifier BUNDLE_PROGRESS_BAR_FULL_TEXTURE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_full");
    private static final Identifier BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_back");
    private static final Identifier BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_front");
    private static final Identifier BUNDLE_SLOT_BACKGROUND_TEXTURE = Identifier.withDefaultNamespace("container/bundle/slot_background");
    private static final Component BUNDLE_FULL = Component.translatable("item.minecraft.bundle.full");
    private static final Component BUNDLE_EMPTY = Component.translatable("item.minecraft.bundle.empty");
    private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.canadamod.thermos.empty.description");
    private final ThermosContentsComponent thermosContents;

    public ThermosTooltipComponent(ThermosContentsComponent bundleContents) {
        this.thermosContents = bundleContents;
    }

    public int getHeight(Font textRenderer) {
        return this.thermosContents.isEmpty() ? getHeightOfEmpty(textRenderer) : this.getHeightOfNonEmpty();
    }

    public int getWidth(Font textRenderer) {
        return 96;
    }

    public boolean showTooltipWithItemInHand() {
        return true;
    }

    private static int getHeightOfEmpty(Font textRenderer) {
        return getDescriptionHeight(textRenderer) + 13 + 8;
    }

    private int getHeightOfNonEmpty() {
        return this.getRowsHeight() + 13 + 8;
    }

    private int getRowsHeight() {
        return this.getRows() * 24;
    }

    private int getXMargin(int width) {
        return (width - 96) / 2;
    }

    private int getRows() {
        return Mth.positiveCeilDiv(this.getNumVisibleSlots(), 4);
    }

    private int getNumVisibleSlots() {
        return Math.min(12, this.thermosContents.size());
    }

    public void extractImage(Font textRenderer, int x, int y, int width, int height, GuiGraphicsExtractor context) {
        if (this.thermosContents.isEmpty()) {
            this.drawEmptyTooltip(textRenderer, x, y, width, height, context);
        } else {
            this.drawNonEmptyTooltip(textRenderer, x, y, width, height, context);
        }

    }

    private void drawEmptyTooltip(Font textRenderer, int x, int y, int width, int height, GuiGraphicsExtractor context) {
        drawEmptyDescription(x + this.getXMargin(width), y, textRenderer, context);
        this.drawProgressBar(x + this.getXMargin(width), y + getDescriptionHeight(textRenderer) + 4, textRenderer, context);
    }

    private void drawNonEmptyTooltip(Font textRenderer, int x, int y, int width, int height, GuiGraphicsExtractor context) {
        boolean bl = this.thermosContents.size() > 12;
        List<ItemStack> list = this.firstStacksInContents(this.thermosContents.getNumberOfStacksShown());
        int i = x + this.getXMargin(width) + 96;
        int j = y + this.getRows() * 24;
        int k = 1;

        for(int l = 1; l <= this.getRows(); ++l) {
            for(int m = 1; m <= 4; ++m) {
            int n = i - m * 24;
            int o = j - l * 24;
            if (shouldDrawExtraItemsCount(bl, m, l)) {
                drawExtraItemsCount(n, o, this.numContentItemsAfter(list), textRenderer, context);
            } else if (shouldDrawItem(list, k)) {
                this.drawItem(k, n, o, list, k, textRenderer, context);
                ++k;
            }
            }
        }

        this.drawSelectedItemTooltip(textRenderer, context, x, y, width);
        this.drawProgressBar(x + this.getXMargin(width), y + this.getRowsHeight() + 4, textRenderer, context);
    }

    private List<ItemStack> firstStacksInContents(int numberOfStacksShown) {
        int i = Math.min(this.thermosContents.size(), numberOfStacksShown);
        return this.thermosContents.stream().toList().subList(0, i);
    }

    private static boolean shouldDrawExtraItemsCount(boolean hasMoreItems, int column, int row) {
        return hasMoreItems && column * row == 1;
    }

    private static boolean shouldDrawItem(List<ItemStack> items, int itemIndex) {
        return items.size() >= itemIndex;
    }

    private int numContentItemsAfter(List<ItemStack> items) {
        return this.thermosContents.stream().skip((long)items.size()).mapToInt(ItemStack::getCount).sum();
    }

    private void drawItem(int index, int x, int y, List<ItemStack> stacks, int seed, Font textRenderer, GuiGraphicsExtractor drawContext) {
        int i = stacks.size() - index;
        boolean bl = i == this.thermosContents.getSelectedStackIndex();
        ItemStack itemStack = (ItemStack)stacks.get(i);
        if (bl) {
            drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, BUNDLE_SLOT_HIGHLIGHT_BACK_TEXTURE, x, y, 24, 24);
        } else {
            drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, BUNDLE_SLOT_BACKGROUND_TEXTURE, x, y, 24, 24);
        }

        drawContext.item(itemStack, x + 4, y + 4, seed);
        drawContext.itemDecorations(textRenderer, itemStack, x + 4, y + 4);
        if (bl) {
            drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, BUNDLE_SLOT_HIGHLIGHT_FRONT_TEXTURE, x, y, 24, 24);
        }

    }

    private static void drawExtraItemsCount(int x, int y, int numExtra, Font textRenderer, GuiGraphicsExtractor drawContext) {
        drawContext.centeredText(textRenderer, "+" + numExtra, x + 12, y + 10, 16777215);
    }

    private void drawSelectedItemTooltip(Font textRenderer, GuiGraphicsExtractor drawContext, int x, int y, int width) {
        if (this.thermosContents.hasSelectedStack()) {
            ItemStack itemStack = this.thermosContents.get(this.thermosContents.getSelectedStackIndex());
            Component text = itemStack.getStyledHoverName();
            int i = textRenderer.width(text.getVisualOrderText());
            int j = x + width / 2 - 12;
            drawContext.setTooltipForNextFrame(textRenderer, text, j - i / 2, y - 15, itemStack.get(DataComponents.TOOLTIP_STYLE));
        }

    }

    private void drawProgressBar(int x, int y, Font textRenderer, GuiGraphicsExtractor drawContext) {
        drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, this.getProgressBarFillTexture(), x + 1, y, this.getProgressBarFill(), 13);
        drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, BUNDLE_PROGRESS_BAR_BORDER_TEXTURE, x, y, 96, 13);
        Component text = this.getProgressBarLabel();
        if (text != null) {
            drawContext.centeredText(textRenderer, text, x + 48, y + 3, 16777215);
        }

    }

    private static void drawEmptyDescription(int x, int y, Font textRenderer, GuiGraphicsExtractor drawContext) {
        drawContext.textWithWordWrap(textRenderer, BUNDLE_EMPTY_DESCRIPTION, x, y, 96, 11184810);
    }

    private static int getDescriptionHeight(Font textRenderer) {
        int var10000 = textRenderer.split(BUNDLE_EMPTY_DESCRIPTION, 96).size();
        Objects.requireNonNull(textRenderer);
        return var10000 * 9;
    }

    private int getProgressBarFill() {
        return Mth.clamp(Mth.mulAndTruncate(this.thermosContents.getOccupancy(), 94), 0, 94);
    }

    private Identifier getProgressBarFillTexture() {
        return this.thermosContents.getOccupancy().compareTo(Fraction.ONE) >= 0 ? BUNDLE_PROGRESS_BAR_FULL_TEXTURE : BUNDLE_PROGRESS_BAR_FILL_TEXTURE;
    }

    @Nullable
    private Component getProgressBarLabel() {
        if (this.thermosContents.isEmpty()) {
            return BUNDLE_EMPTY;
        } else {
            return this.thermosContents.getOccupancy().compareTo(Fraction.ONE) >= 0 ? BUNDLE_FULL : null;
        }
    }
}
