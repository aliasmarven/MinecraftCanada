package xen42.canadamod.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import xen42.canadamod.CanadaMod;

public class CookingPotHandledScreen extends AbstractRecipeBookScreen<CookingPotScreenHandler> {
    private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.withDefaultNamespace("container/furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.withDefaultNamespace("container/furnace/burn_progress");
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/gui/cooking_pot_gui.png");

    public CookingPotHandledScreen(CookingPotScreenHandler handler, Inventory inventory, Component title) {
        super(handler, new CookingPotRecipeBookWidget(handler), inventory, title);
        this.recipeBookWidget = (CookingPotRecipeBookWidget)this.recipeBookComponent;
    }

    private final CookingPotRecipeBookWidget recipeBookWidget;

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 108, this.height / 2 - 28);
    }

    @Override
    public void extractContents(
            GuiGraphicsExtractor context,
            int mouseX, int mouseY, float deltaTicks) {

        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;

        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
            i, j, 0.0F, 0.0F,
            this.imageWidth, this.imageHeight, 256, 256);

        int n = Mth.ceil(this.menu.getFuelProgress() * 13.0F) + 1;
        context.blitSprite(RenderPipelines.GUI_TEXTURED,
            LIT_PROGRESS_TEXTURE,
            14, 14, 0, 14 - n,
            i + 81, j + 58 - n, 14, n);

        int l = Mth.ceil(this.menu.getCookProgress() * 24.0F);
        context.blitSprite(RenderPipelines.GUI_TEXTURED,
            BURN_PROGRESS_TEXTURE,
            24, 16, 0, 0,
            i + 109, j + 34, l, 16);

        super.extractContents(context, mouseX, mouseY, deltaTicks);
    }

}
