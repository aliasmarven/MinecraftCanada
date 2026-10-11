package xen42.canadamod;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class MapleLeavesFactory implements ParticleProvider<SimpleParticleType> {

    private final FabricSpriteSet spriteSet;

    public MapleLeavesFactory(FabricSpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(
            SimpleParticleType type,
            ClientLevel level,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            RandomSource random) {

        return new MapleLeafParticle(
            level, x, y, z, spriteSet.get(random)
        );
    }

    private static final class MapleLeafParticle extends FallingLeavesParticle {

        private MapleLeafParticle(
                ClientLevel level,
                double x, double y, double z,
                TextureAtlasSprite sprite) {

            super(
                level, x, y, z, sprite,
                0.25F, 2.0F, false, true, 1.0F, 0.0F
            );
        }
    }
}
