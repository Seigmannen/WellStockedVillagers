package com.seigmannen.wellstockedvillagers.mixin;

import com.seigmannen.wellstockedvillagers.config.ModConfig;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Vanilla hardcodes the restock limits as constants:
 * allowedToRestock(): numberOfRestocksToday == 0 || (numberOfRestocksToday < 2 && gameTime > lastRestockGameTime + 2400)
 * catchUpDemand():    missed = 2 - numberOfRestocksToday
 */
@Mixin(Villager.class)
public abstract class VillagerMixin {
	@ModifyConstant(method = "allowedToRestock", constant = @Constant(intValue = 2))
	private int wellstockedvillagers$restocksPerDay(int original) {
		return ModConfig.get().restocksPerDay;
	}

	@ModifyConstant(method = "allowedToRestock", constant = @Constant(longValue = 2400L))
	private long wellstockedvillagers$minTicksBetweenRestocks(long original) {
		return ModConfig.get().minTicksBetweenRestocks();
	}

	@ModifyConstant(method = "catchUpDemand", constant = @Constant(intValue = 2))
	private int wellstockedvillagers$catchUpRestocksPerDay(int original) {
		return ModConfig.get().restocksPerDay;
	}
}
