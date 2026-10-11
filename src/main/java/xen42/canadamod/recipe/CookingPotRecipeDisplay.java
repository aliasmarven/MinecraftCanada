package xen42.canadamod.recipe;

import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay.ItemSlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay.ItemStackSlotDisplay;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CookingPotRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay containers, ItemStackSlotDisplay result, ItemSlotDisplay craftingStation)
	implements RecipeDisplay {

	public static final MapCodec<CookingPotRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(CookingPotRecipeDisplay::ingredients),
				SlotDisplay.CODEC.fieldOf("containers").forGetter(CookingPotRecipeDisplay::containers),
				ItemStackSlotDisplay.MAP_CODEC.fieldOf("result").forGetter(CookingPotRecipeDisplay::result),
				ItemSlotDisplay.MAP_CODEC.fieldOf("crafting_station").forGetter(CookingPotRecipeDisplay::craftingStation)
			)
			.apply(instance, CookingPotRecipeDisplay::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipeDisplay> PACKET_CODEC = StreamCodec.composite(
		SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()),
		CookingPotRecipeDisplay::ingredients,
		SlotDisplay.STREAM_CODEC,
		CookingPotRecipeDisplay::containers,
		ItemStackSlotDisplay.STREAM_CODEC,
		CookingPotRecipeDisplay::result,
		ItemSlotDisplay.STREAM_CODEC,
		CookingPotRecipeDisplay::craftingStation,
		CookingPotRecipeDisplay::new
	);
	public static final RecipeDisplay.Type<CookingPotRecipeDisplay> SERIALIZER = new RecipeDisplay.Type<>(CODEC, PACKET_CODEC);

    @Override
    public Type<? extends RecipeDisplay> type() {
        return SERIALIZER;
    }

    @Override
	public boolean isEnabled(FeatureFlagSet features) {
		return this.ingredients.stream().allMatch(ingredient -> ingredient.isEnabled(features)) && RecipeDisplay.super.isEnabled(features) && containers.isEnabled(features);
	}
}
