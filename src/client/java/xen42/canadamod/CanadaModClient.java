package xen42.canadamod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.function.Supplier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import xen42.canadamod.armor.BeaverHatModel;
import xen42.canadamod.armor.MooseHatModel;
import xen42.canadamod.block.MooseSkullBlock;
import xen42.canadamod.entities.BeaverChopTreeEffectPayload;
import xen42.canadamod.entities.BeaverChopTreeGoal;
import xen42.canadamod.entities.BeaverEntity;
import xen42.canadamod.entities.MapleBoatEntity;
import xen42.canadamod.entity.BeaverEntityModel;
import xen42.canadamod.entity.BeaverEntityRenderer;
import xen42.canadamod.entity.DuckEntityModel;
import xen42.canadamod.entity.DuckEntityRenderer;
import xen42.canadamod.entity.GooseEntityModel;
import xen42.canadamod.entity.GooseEntityRenderer;
import xen42.canadamod.entity.GrizzlyEntityModel;
import xen42.canadamod.entity.GrizzlyEntityRenderer;
import xen42.canadamod.entity.MooseEntityModel;
import xen42.canadamod.entity.MooseEntityRenderer;
import xen42.canadamod.entity.MooseSkullBlockEntityRenderer;
import xen42.canadamod.screen.CookingPotHandledScreen;

public class CanadaModClient implements ClientModInitializer {
	public static final ModelLayerLocation MAPLE_BOAT = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "boat/maple"), "main");
	public static final ModelLayerLocation MAPLE_CHEST_BOAT = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "chest_boat/maple"), "main");
	public static final ModelLayerLocation MODEL_BEAVER_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "beaver"), "main");
	public static final ModelLayerLocation MODEL_MOOSE_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "moose"), "main");
	public static final ModelLayerLocation MODEL_DUCK_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "duck"), "main");
	public static final ModelLayerLocation MODEL_GOOSE_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "goose"), "main");
	public static final ModelLayerLocation MODEL_GRIZZLY_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "grizzly"), "main");

	public class MapleBoat implements ModelLayerRegistry.TexturedLayerDefinitionProvider {
		@Override
		public LayerDefinition createLayerDefinition() {
			return BoatModel.createBoatModel();
		}
	}

	public class MapleChestBoat implements ModelLayerRegistry.TexturedLayerDefinitionProvider {
		@Override
		public LayerDefinition createLayerDefinition() {
			return BoatModel.createChestBoatModel();
		}
	}

	@Override
	public void onInitializeClient() {







		EntityRendererRegistry.register(MapleBoatEntity.MAPLE_BOAT, context -> new BoatRenderer(context, MAPLE_BOAT));
		EntityRendererRegistry.register(MapleBoatEntity.MAPLE_CHEST_BOAT, context -> new BoatRenderer(context, MAPLE_CHEST_BOAT));
		ModelLayerRegistry.registerModelLayer(MAPLE_BOAT, new MapleBoat());
		ModelLayerRegistry.registerModelLayer(MAPLE_CHEST_BOAT, new MapleChestBoat());

		ParticleProviderRegistry.getInstance().register(CanadaBlocks.MAPLE_LEAF_PARTICLE, MapleLeavesFactory::new);

		BlockEntityRenderers.register(CanadaBlocks.MAPLE_SIGN_BLOCK_ENTITY, StandingSignRenderer::new);
		BlockEntityRenderers.register(CanadaBlocks.MAPLE_HANGING_SIGN_BLOCK_ENTITY, HangingSignRenderer::new);

		MenuScreens.register(CanadaMod.COOKING_POT_SCREEN_HANDLER_TYPE, CookingPotHandledScreen::new);

		EntityRendererRegistry.register(CanadaMod.BEAVER_ENTITY, context -> new BeaverEntityRenderer(context));
		ModelLayerRegistry.registerModelLayer(MODEL_BEAVER_LAYER, BeaverEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(CanadaMod.MOOSE_ENTITY, context -> new MooseEntityRenderer(context));
		ModelLayerRegistry.registerModelLayer(MODEL_MOOSE_LAYER, MooseEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(CanadaMod.DUCK_ENTITY, context -> new DuckEntityRenderer(context));
		ModelLayerRegistry.registerModelLayer(MODEL_DUCK_LAYER, net.minecraft.client.model.animal.chicken.AdultChickenModel::createBodyLayer);

		EntityRendererRegistry.register(CanadaMod.GOOSE_ENTITY, context -> new GooseEntityRenderer(context));
		ModelLayerRegistry.registerModelLayer(MODEL_GOOSE_LAYER, GooseEntityModel::createBodyLayer);

		EntityRendererRegistry.register(CanadaMod.GRIZZLY_ENTITY, context -> new GrizzlyEntityRenderer(context));
		ModelLayerRegistry.registerModelLayer(MODEL_GRIZZLY_LAYER, GrizzlyEntityModel::getTexturedModelData);

		ArmorRenderer.register(new CustomArmorRenderer(BeaverHatModel::getModel), CanadaItems.BEAVER_HELMET);
		ArmorRenderer.register(new CustomArmorRenderer(MooseHatModel::getModel), CanadaItems.MOOSE_HELMET);
		ArmorRenderer.register(new BlockOnHeadArmorRenderer(CanadaBlocks.MOOSE_HEAD), CanadaItems.MOOSE_HEAD);

		BlockEntityRenderers.register(CanadaMod.MOOSE_HEAD_ENTITY, MooseSkullBlockEntityRenderer::new);

		ClientPlayNetworking.registerGlobalReceiver(BeaverChopTreeEffectPayload.PAYLOAD_ID, (payload, context) -> {
			context.client().execute(() -> {
				// Animations and rendering all done on the client side, but Goal behaviours run on the server
				var beaver = (BeaverEntity)context.client().level.getEntity(payload.id);
				if (beaver != null) {
					beaver.isChopping = payload.stage != -1;
				}
				context.client().level.destroyBlockProgress(payload.id, payload.pos, payload.stage);

				// Don't play sound when setting a guy to -1
				if (payload.stage > 0) {
					context.client().level.playLocalSound(
						payload.pos, SoundType.WOOD.getHitSound(), SoundSource.BLOCKS, 1.0f, 1.0f, false
					);
				}
			});
		});
	}


	private class CustomArmorRenderer implements ArmorRenderer {
		private Supplier<LayerDefinition> model;

		public CustomArmorRenderer(Supplier<LayerDefinition> model) {
			this.model = model;
		}

		@Override
		public void render(PoseStack matrices, SubmitNodeCollector vertexConsumers, ItemStack stack,
				HumanoidRenderState bipedEntityRenderState, EquipmentSlot slot, int light,
				HumanoidModel<HumanoidRenderState> contextModel) {
			if (slot != EquipmentSlot.HEAD) {
				return;
			}

			var name = stack.getItem().toString().split(":")[1];

			ModelPart part = model.get().bakeRoot().getChild("hat");
			part.loadPose(contextModel.getHead().storePose());
			vertexConsumers.submitModelPart(
                part,
                matrices,
                RenderTypes.armorCutoutNoCull(Identifier.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/armor/" + name + ".png")),
                light,
                OverlayTexture.NO_OVERLAY,
                new UvMapping() {
                    @Override
                    public float getU(float u) {
                        return u;
                    }

                    @Override
                    public float getV(float v) {
                        return v;
                    }
                }
            );

			ItemStackRenderState itemState = new ItemStackRenderState();

            Minecraft.getInstance().getItemModelResolver().updateForTopItem(
                itemState,
                stack,
                ItemDisplayContext.HEAD,
                Minecraft.getInstance().level,
                null,
                0
            );

            itemState.submit(
                matrices,
                vertexConsumers,
                light,
                OverlayTexture.NO_OVERLAY,
                -1
            );
		}
	}

	private class BlockOnHeadArmorRenderer implements ArmorRenderer {
		private Block block;
        private final BlockModelResolver modelResolver =
            new BlockModelResolver(Minecraft.getInstance().getModelManager());

		public BlockOnHeadArmorRenderer(Block block) {
			this.block = block;
		}

		@Override
		public void render(PoseStack matrices, SubmitNodeCollector vertexConsumers, ItemStack stack,
				HumanoidRenderState bipedEntityRenderState, EquipmentSlot slot, int light,
				HumanoidModel<HumanoidRenderState> contextModel) {
			if (slot != EquipmentSlot.HEAD) {
				return;
			}

			matrices.pushPose();

			var head = contextModel.getHead();
			matrices.translate(head.x / 16.0f, head.y / 16.0f, head.z / 16.0f);
			matrices.mulPose(new org.joml.Matrix4f().rotationZ(head.zRot));
			matrices.mulPose(new org.joml.Matrix4f().rotationY(head.yRot));
			matrices.mulPose(new org.joml.Matrix4f().rotationX(head.xRot));
			matrices.translate(0.5f, -0.025f, -0.75f);
			matrices.mulPose(new org.joml.Matrix4f().rotationZ((float)Math.PI));
			matrices.scale(1.05f, 1.05f, 1.05f);
			matrices.translate(-0.025f, -0.025f, -0.025f);

			var state = block.defaultBlockState();
			if (state.is(CanadaBlocks.MOOSE_HEAD)) {
				state = state.setValue(MooseSkullBlock.WEIRD_HACK, false);
			}

			BlockModelRenderState modelState = new BlockModelRenderState();
            this.modelResolver.update(
                modelState,
                state,
                BlockDisplayContext.create()
            );
            modelState.submit(
                matrices,
                vertexConsumers,
                light,
                OverlayTexture.NO_OVERLAY,
                -1
            );

			matrices.popPose();
		}
	}
}
