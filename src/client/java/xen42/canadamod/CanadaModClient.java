package xen42.canadamod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.function.Supplier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
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
	public static final ModelLayerLocation MAPLE_BOAT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "boat/maple"), "main");
	public static final ModelLayerLocation MAPLE_CHEST_BOAT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "chest_boat/maple"), "main");
	public static final ModelLayerLocation MODEL_BEAVER_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "beaver"), "main");
	public static final ModelLayerLocation MODEL_MOOSE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "moose"), "main");
	public static final ModelLayerLocation MODEL_DUCK_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "duck"), "main");
	public static final ModelLayerLocation MODEL_GOOSE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "goose"), "main");
	public static final ModelLayerLocation MODEL_GRIZZLY_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "grizzly"), "main");

	public class MapleBoat implements EntityModelLayerRegistry.TexturedModelDataProvider {
		@Override
		public LayerDefinition createModelData() {
			return BoatModel.createBoatModel();
		}
	}

	public class MapleChestBoat implements EntityModelLayerRegistry.TexturedModelDataProvider {
		@Override
		public LayerDefinition createModelData() {
			return BoatModel.createChestBoatModel();
		}
	}

	@Override
	public void onInitializeClient() {
		addCustomWoodTypeTexture(CanadaBlocks.MAPLE_WOOD_TYPE);

		BlockRenderLayerMap.INSTANCE.putBlock(CanadaBlocks.MAPLE_DOOR, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CanadaBlocks.MAPLE_SAPLING, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CanadaBlocks.POTTED_MAPLE_SAPLING, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CanadaBlocks.TREE_TAP, RenderType.cutout());

		EntityRendererRegistry.register(MapleBoatEntity.MAPLE_BOAT, context -> new BoatRenderer(context, MAPLE_BOAT));
		EntityRendererRegistry.register(MapleBoatEntity.MAPLE_CHEST_BOAT, context -> new BoatRenderer(context, MAPLE_CHEST_BOAT));
		EntityModelLayerRegistry.registerModelLayer(MAPLE_BOAT, new MapleBoat());
		EntityModelLayerRegistry.registerModelLayer(MAPLE_CHEST_BOAT, new MapleChestBoat());

		ParticleFactoryRegistry.getInstance().register(CanadaBlocks.MAPLE_LEAF_PARTICLE, MapleLeavesFactory::new);

		BlockEntityRenderers.register(CanadaBlocks.MAPLE_SIGN_BLOCK_ENTITY, SignRenderer::new);
		BlockEntityRenderers.register(CanadaBlocks.MAPLE_HANGING_SIGN_BLOCK_ENTITY, HangingSignRenderer::new);

		MenuScreens.register(CanadaMod.COOKING_POT_SCREEN_HANDLER_TYPE, CookingPotHandledScreen::new);

		EntityRendererRegistry.register(CanadaMod.BEAVER_ENTITY, context -> new BeaverEntityRenderer(context));
		EntityModelLayerRegistry.registerModelLayer(MODEL_BEAVER_LAYER, BeaverEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(CanadaMod.MOOSE_ENTITY, context -> new MooseEntityRenderer(context));
		EntityModelLayerRegistry.registerModelLayer(MODEL_MOOSE_LAYER, MooseEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(CanadaMod.DUCK_ENTITY, context -> new DuckEntityRenderer(context));
		EntityModelLayerRegistry.registerModelLayer(MODEL_DUCK_LAYER, DuckEntityModel::createBodyLayer);

		EntityRendererRegistry.register(CanadaMod.GOOSE_ENTITY, context -> new GooseEntityRenderer(context));
		EntityModelLayerRegistry.registerModelLayer(MODEL_GOOSE_LAYER, GooseEntityModel::createBodyLayer);

		EntityRendererRegistry.register(CanadaMod.GRIZZLY_ENTITY, context -> new GrizzlyEntityRenderer(context));
		EntityModelLayerRegistry.registerModelLayer(MODEL_GRIZZLY_LAYER, GrizzlyEntityModel::getTexturedModelData);

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
				context.client().levelRenderer.destroyBlockProgress(payload.id, payload.pos, payload.stage);

				// Don't play sound when setting a guy to -1
				if (payload.stage > 0) {
					context.client().level.playLocalSound(
						payload.pos, SoundType.WOOD.getHitSound(), SoundSource.BLOCKS, 1.0f, 1.0f, false
					);
				}
			});
		});
	}

	public void addCustomWoodTypeTexture(WoodType type) {
		ResourceLocation textureId = ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "entity/signs/" + type.name());
		Material spriteId = new Material(Sheets.SIGN_MATERIALS.get(WoodType.OAK).atlasLocation(), textureId);
		Sheets.SIGN_MATERIALS.put(type, spriteId);

		ResourceLocation hangingTextureId = ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "entity/signs/hanging/" + type.name());
		Material hangingSpriteId = new Material(Sheets.HANGING_SIGN_MATERIALS.get(WoodType.OAK).atlasLocation(), hangingTextureId);
		Sheets.HANGING_SIGN_MATERIALS.put(type, hangingSpriteId);
	}

	private class CustomArmorRenderer implements ArmorRenderer {
		private Supplier<LayerDefinition> model;

		public CustomArmorRenderer(Supplier<LayerDefinition> model) {
			this.model = model;
		}

		@Override
		public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack,
				HumanoidRenderState bipedEntityRenderState, EquipmentSlot slot, int light,
				HumanoidModel<HumanoidRenderState> contextModel) {
			if (slot != EquipmentSlot.HEAD) {
				return;
			}

			var name = stack.getItem().toString().split(":")[1];

			ModelPart part = model.get().bakeRoot().getChild("hat");
			part.copyFrom(contextModel.getHead());
			part.render(matrices, vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "textures/armor/" + name + ".png"))),
				light, OverlayTexture.NO_OVERLAY);

			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.HEAD, light, OverlayTexture.NO_OVERLAY, matrices,
				vertexConsumers, Minecraft.getInstance().level, 0);
		}
	}

	private class BlockOnHeadArmorRenderer implements ArmorRenderer {
		private Block block;

		public BlockOnHeadArmorRenderer(Block block) {
			this.block = block;
		}

		@Override
		public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack,
				HumanoidRenderState bipedEntityRenderState, EquipmentSlot slot, int light,
				HumanoidModel<HumanoidRenderState> contextModel) {
			if (slot != EquipmentSlot.HEAD) {
				return;
			}

			matrices.pushPose();

			var head = contextModel.getHead();
			matrices.translate(head.x / 16.0f, head.y / 16.0f, head.z / 16.0f);
			matrices.mulPose(Axis.ZP.rotation(head.zRot));
			matrices.mulPose(Axis.YP.rotation(head.yRot));
			matrices.mulPose(Axis.XP.rotation(head.xRot));
			matrices.translate(0.5f, -0.025f, -0.75f);
			matrices.mulPose(Axis.ZP.rotation((float)Math.PI));
			matrices.scale(1.05f, 1.05f, 1.05f);
			matrices.translate(-0.025f, -0.025f, -0.025f);

			var state = block.defaultBlockState();
			if (state.is(CanadaBlocks.MOOSE_HEAD)) {
				state = state.setValue(MooseSkullBlock.WEIRD_HACK, false);
			}

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, matrices, vertexConsumers,
				light, OverlayTexture.NO_OVERLAY);

			matrices.popPose();
		}
	}
}