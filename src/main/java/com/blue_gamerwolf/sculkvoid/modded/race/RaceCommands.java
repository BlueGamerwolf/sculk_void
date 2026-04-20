package com.blue_gamerwolf.sculkvoid.modded.race;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.races.registry.RaceRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.data.storage.RaceData;
import com.blue_gamerwolf.sculkvoid.modded.race.network.RaceSelectionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RaceCommands {

    private static final SuggestionProvider<CommandSourceStack> RACE_SUGGESTIONS =
            (context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(RaceRegistry.getAll().keySet(), builder);

    private static final SuggestionProvider<CommandSourceStack> SUBRACE_SUGGESTIONS =
            (context, builder) -> {
                String raceId = StringArgumentType.getString(context, "race");
                RaceModule module = RaceRegistry.get(raceId);
                if (module == null) {
                    return builder.buildFuture();
                }

                return net.minecraft.commands.SharedSuggestionProvider.suggest(
                        module.getSubRaces().stream().map(SubRace::id),
                        builder
                );
            };

    private RaceCommands() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sculkvoid")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("race")
                        .then(Commands.literal("list").executes(RaceCommands::listRaces))
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> getRace(context, EntityArgument.getPlayer(context, "player")))))
                        .then(Commands.literal("clear")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> clearRace(context, EntityArgument.getPlayer(context, "player")))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("race", StringArgumentType.word())
                                                .suggests(RACE_SUGGESTIONS)
                                                .then(Commands.argument("subrace", StringArgumentType.word())
                                                        .suggests(SUBRACE_SUGGESTIONS)
                                                        .executes(context -> setRace(
                                                                context,
                                                                EntityArgument.getPlayer(context, "player"),
                                                                StringArgumentType.getString(context, "race"),
                                                                StringArgumentType.getString(context, "subrace")
                                                        ))))))
                        .then(Commands.literal("random")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> randomRace(context, EntityArgument.getPlayer(context, "player")))))));
    }

    private static int getRace(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        String race = RaceData.hasRace(player) ? RaceData.getRaceId(player) : "none";
        String subRace = RaceData.hasSubRace(player) ? RaceData.getSubRaceId(player) : "none";
        context.getSource().sendSuccess(() -> Component.literal(
                player.getGameProfile().getName() + " race=" + race + ", subrace=" + subRace
        ), false);
        return 1;
    }

    private static int clearRace(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        RaceData.clear(player);
        context.getSource().sendSuccess(() -> Component.literal(
                "Cleared race data for " + player.getGameProfile().getName()
        ), true);
        return 1;
    }

    private static int setRace(CommandContext<CommandSourceStack> context,
                               ServerPlayer player,
                               String raceId,
                               String subRaceId) {
        if (!RaceSelectionHandler.assignRace(player, raceId, subRaceId)) {
            context.getSource().sendFailure(Component.literal("Invalid race or subrace selection"));
            return 0;
        }

        context.getSource().sendSuccess(() -> Component.literal(
                "Set " + player.getGameProfile().getName() + " race=" + RaceData.getRaceId(player) + ", subrace=" + RaceData.getSubRaceId(player)
        ), true);
        return 1;
    }

    private static int randomRace(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        if (!RaceSelectionHandler.assignRandomRace(player)) {
            context.getSource().sendFailure(Component.literal("No valid random race available"));
            return 0;
        }

        context.getSource().sendSuccess(() -> Component.literal(
                "Randomized " + player.getGameProfile().getName() + " to race=" + RaceData.getRaceId(player) + ", subrace=" + RaceData.getSubRaceId(player)
        ), true);
        return 1;
    }

    private static int listRaces(CommandContext<CommandSourceStack> context) {
        StringBuilder builder = new StringBuilder("Story: ");
        appendCategory(builder, RaceCategory.STORY);
        builder.append(" || Extra: ");
        appendCategory(builder, RaceCategory.EXTRA);
        context.getSource().sendSuccess(() -> Component.literal(builder.toString()), false);
        return 1;
    }

    private static void appendCategory(StringBuilder builder, RaceCategory category) {
        boolean first = true;
        for (RaceModule module : RaceManager.getRacesByCategory(category)) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(module.displayName())
                    .append(" (")
                    .append(module.id())
                    .append(")");
        }
    }
}
