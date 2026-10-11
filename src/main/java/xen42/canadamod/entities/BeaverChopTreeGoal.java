package xen42.canadamod.entities;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import xen42.canadamod.CanadaBlocks;
import xen42.canadamod.CanadaMod;

public class BeaverChopTreeGoal extends Goal {
    public static Map<LeavesBlock, SaplingBlock> saplingMap = new HashMap();

    private final BeaverEntity beaver;

    @Nullable
    private Path pathToTrunk;

    @Nullable
    private SaplingBlock sapling;

    private boolean isChoppingTree;

    private final int MAX_BREAKING_TICKS = 40;
    private int choppingTicks = 0;

    public BeaverChopTreeGoal(BeaverEntity beaver) {
        this.beaver = beaver;
    }

    public boolean canUse() {
        if (!beaver.canChopTree()) {
            return false;
        }

        if (!(beaver.level() instanceof ServerLevel serverWorld) || !serverWorld.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        }

        if (!this.beaver.isFrenzied() && this.beaver.getRandom().nextInt(reducedTickDelay(20)) != 0) {
            return false;
        }

        this.beaver.setChoppingTree(findChoppableTree(5));
        if (this.beaver.getChoppingTreePos() == null) {
            return false;
        }

        pathToTrunk = beaver.getNavigation().createPath(this.beaver.getChoppingTreePos().getX(), this.beaver.getChoppingTreePos().getY(),
            this.beaver.getChoppingTreePos().getZ(), 0);
        if (pathToTrunk == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (isChoppingTree) return true;

        if (this.beaver.getChoppingTreePos() == null) return false;

        return !this.beaver.getNavigation().isDone() || this.beaver.level().getBlockState(this.beaver.getChoppingTreePos()).isAir();
    }

    @Override
    public void start() {
        this.beaver.getNavigation().moveTo(this.pathToTrunk, 1f);
    }

    @Override
    public void stop() {
        // In case somebody else broke it
        this.beaver.stopChopping();
        this.sapling = null;
    }

    private BlockPos findChoppableTree(int radius) {
        for (var dx = -radius; dx <= radius; dx++) {
            for (var dz = -radius; dz <= radius; dz++) {
                for (var dy = -1; dy <= 1; dy++) {
                    var trunkPos = beaver.blockPosition().offset(dx, dy, dz);

                    if (isTreeTrunk(trunkPos)) {
                        return trunkPos;
                    }
                }
            }
        }
        return null;
    }

    private boolean isTreeTrunk(BlockPos pos) {
        var world = beaver.level();
        var trunk = world.getBlockState(pos);
        this.sapling = null;

        // If its a log with dirt under it, it may be the start of a tree!
        // From this we move up to make sure it has leaves on the top, else we might be breaking somebody's house
        var dirt = world.getBlockState(pos.below());
        if (trunk.is(BlockTags.LOGS) && (dirt.is(BlockTags.DIRT) || dirt.is(Blocks.GRASS_BLOCK) || dirt.is(Blocks.PODZOL))) {
            // Move up to find leaves
            for (int i = 0; i < 20; i++) {
                var topBlockPos = pos.offset(0, i, 0);
                var topBlock = world.getBlockState(topBlockPos);
                // Also checks for tree taps
                if (!noLogsNextToBlock(topBlockPos)) {
                    return false;
                }
                if (isNaturalLeaf(topBlock)) {
                    this.sapling = getSaplingFromLeaf(topBlockPos);
                    return true;
                }
                // Make sure it is a continuous tree
                else if (!topBlock.is(BlockTags.LOGS)) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean isNaturalLeaf(BlockState state) {
        return state.is(BlockTags.LEAVES) && state.hasProperty(BlockStateProperties.PERSISTENT) && !state.getValue(BlockStateProperties.PERSISTENT);
    }

    @Nullable
    private SaplingBlock getSaplingFromLeaf(BlockPos pos) {
        var blockState = beaver.level().getBlockState(pos);
        var leafBlock = (LeavesBlock)blockState.getBlock();

        if (leafBlock == null) {
            return null;
        }

        if (saplingMap.containsKey(leafBlock)) {
            return saplingMap.get(leafBlock);
        }
        else {
            // This is straight goofy ok we need to simulate like 40 leaf drops to PROBABLY get a sapling but even then
            for (int i = 0; i < 60; i++) {
                for (var item : Block.getDrops(blockState, (ServerLevel)beaver.level(), pos, null)) {
                    if (item.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SaplingBlock sapling) {
                        saplingMap.put(leafBlock, sapling);
                        return sapling;
                    }
                }
            }
        }

        return null;
    }

    private boolean noLogsNextToBlock(BlockPos pos) {
        var world = this.beaver.level();
        for (int ddx = -1; ddx <= 1; ddx++) {
            for (int ddz = -1; ddz <= 1; ddz++) {
                if (ddx == 0 && ddz == 0) {
                    continue;
                }
                var block = world.getBlockState(pos.offset(ddx, 0, ddz));
                if (block.is(BlockTags.LOGS) || block.is(CanadaBlocks.TREE_TAP)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void tick() {
        if (isChoppingTree) {
            // Don't let them move away
            this.beaver.getNavigation().stop();
            this.beaver.getLookControl().setLookAt(this.beaver.getChoppingTreePos().getCenter());

            if (!this.beaver.level().getBlockState(this.beaver.getChoppingTreePos()).is(BlockTags.LOGS) ||
                this.beaver.getChoppingTreePos().getCenter().distanceTo(this.beaver.position()) > 2f ||
                this.beaver.getLastDamageSource() != null) {
                // If something else broke it give up, or if we moved away or got hurts
                this.beaver.stopChopping();
                isChoppingTree = false;
            }
            else {
                // Particles and block breaking effects
                var stage = 10 * (MAX_BREAKING_TICKS - choppingTicks) / MAX_BREAKING_TICKS;

                this.beaver.setChoppingProgress(stage);

                choppingTicks--;

                if (choppingTicks <= 0) {
                    breakTree();
                }

                // Make sure the beaver stays here and faces the tree
                // Null check because maybe just broke it
                if (this.beaver.getChoppingTreePos() != null) {

                    this.beaver.getNavigation().stop();
                    this.beaver.getLookControl().setLookAt(this.beaver.getChoppingTreePos().getCenter());
                }
            }
        }
        else if (this.beaver.getChoppingTreePos().getCenter().distanceTo(this.beaver.position()) < 1.6f) {
            // Reached the tree, chop it
            this.isChoppingTree = true;
            choppingTicks = MAX_BREAKING_TICKS;
        }
        if (beaver.isDeadOrDying()) {
            stop();
        }
    }

    private void breakTree() {
        var blockPos = this.beaver.getChoppingTreePos();
        while (this.beaver.level().getBlockState(blockPos).is(BlockTags.LOGS)) {
            this.beaver.level().destroyBlock(blockPos, true);
            blockPos = blockPos.above();
        }
        if (sapling != null) {
            this.beaver.level().setBlockAndUpdate(this.beaver.getChoppingTreePos(), sapling.defaultBlockState());
        }
        this.beaver.stopChopping();
        this.beaver.onChopTree();
        this.isChoppingTree = false;
    }
}

