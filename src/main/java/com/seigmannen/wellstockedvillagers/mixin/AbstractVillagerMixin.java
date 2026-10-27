package com.seigmannen.wellstockedvillagers.mixin;

import com.seigmannen.wellstockedvillagers.config.ModConfig;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Every server-side read of a villager's trades goes through getOffers(), so rewriting maxUses here
 * covers freshly generated trades, trades added on level-up and trades loaded from existing saves.
 */
@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin {
	@Inject(method = "getOffers", at = @At("RETURN"))
	private void wellstockedvillagers$applyMaxUses(CallbackInfoReturnable<MerchantOffers> cir) {
		// Wandering traders also extend AbstractVillager; leave them alone.
		if (!((Object) this instanceof Villager)) {
			return;
		}
		int maxUses = ModConfig.get().maxUses;
		for (MerchantOffer offer : cir.getReturnValue()) {
			if (offer.getMaxUses() != maxUses) {
				((MerchantOfferAccessor) offer).wellstockedvillagers$setMaxUses(maxUses);
			}
		}
	}
}
