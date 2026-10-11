package xen42.canadamod.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import xen42.canadamod.CanadaMod;

public class BeaverChopTreeEffectPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(CanadaMod.MOD_ID, "beaver_chop_tree_effect");
    public static final CustomPacketPayload.Type<BeaverChopTreeEffectPayload> PAYLOAD_ID = new CustomPacketPayload.Type<>(ID);

    public int id;
    public int stage;
    public BlockPos pos;

    public static final StreamCodec<RegistryFriendlyByteBuf, BeaverChopTreeEffectPayload> CODEC = StreamCodec.ofMember(
        BeaverChopTreeEffectPayload::write, BeaverChopTreeEffectPayload::read
    );

    public BeaverChopTreeEffectPayload(int id, int stage, BlockPos pos) {
        this.id = id;
        this.stage = stage;
        this.pos = pos;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_ID;
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(id);
        buf.writeVarInt(stage);
        buf.writeBlockPos(pos);
    }

    private static BeaverChopTreeEffectPayload read(RegistryFriendlyByteBuf buf) {
        var id = buf.readVarInt();
        var stage = buf.readVarInt();
        var pos = buf.readBlockPos();
        return new BeaverChopTreeEffectPayload(id, stage, pos);
    }

}
