package com.seigmannen.wellstockedvillagers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.seigmannen.wellstockedvillagers.config.ModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/**
 * /wsv restocks [count]. Only vanilla argument types, so clients without the mod can use it.
 */
public final class WsvCommand {
	private WsvCommand() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("wsv")
				.then(Commands.literal("restocks")
						.executes(WsvCommand::showRestocks)
						.then(Commands.argument("count", IntegerArgumentType.integer(ModConfig.MIN_RESTOCKS_PER_DAY, ModConfig.MAX_RESTOCKS_PER_DAY))
								.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
								.executes(WsvCommand::setRestocks))));
	}

	private static int showRestocks(CommandContext<CommandSourceStack> context) {
		int restocks = ModConfig.get().restocksPerDay;
		context.getSource().sendSuccess(() -> Component.literal("Villagers can restock " + restocks + " times per day."), false);
		return restocks;
	}

	private static int setRestocks(CommandContext<CommandSourceStack> context) {
		int restocks = IntegerArgumentType.getInteger(context, "count");
		ModConfig.setRestocksPerDay(restocks);
		context.getSource().sendSuccess(() -> Component.literal("Villagers can now restock " + restocks + " times per day."), true);
		return restocks;
	}
}
