package xen42.canadamod;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class MapleLeavesFactory implements ParticleProvider<SimpleParticleType> {

    private final FabricSpriteProvider spriteProvider;

    public MapleLeavesFactory(FabricSpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Override
    public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double x, double y, double z, double g, double h, double i) {
        return new FallingLeavesParticle(clientWorld, x, y, z, this.spriteProvider, 0.25F, 2.0F, false, true, 1.0F, 0.0F);
    }
}
