//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess


object CommandManager {
    var commandList = HashMap<String, PSSCommand>()

    private fun registerCommands() {
        for (command in commandList) {
            ClientCommandRegistrationCallback.EVENT.register(
                ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
                    registerCommand(command.value, dispatcher)
                },
            )
        }
    }

    private fun registerCommand(
        command: PSSCommand,
        dispatcher: CommandDispatcher<FabricClientCommandSource?>
    ) {
        dispatcher.register(
            ClientCommandManager.literal(command.name)
                .registerRequiredArguments(command.requiredArguments)
                .registerRunnable(command.runnable, command.requiredArguments)
                .registerSubcommands(command.subcommands),
        )
    }

    private fun LiteralArgumentBuilder<FabricClientCommandSource?>.registerRequiredArguments(
        arguments: List<ArgumentData<*>>
    ): LiteralArgumentBuilder<FabricClientCommandSource?> = if (arguments.isEmpty()) this
    else this.then(argument(arguments.first().name, arguments.first().type)).registerRequiredArguments(arguments.drop(1))

    private fun LiteralArgumentBuilder<FabricClientCommandSource?>.registerRunnable(
        runnable: PSSCommandRunnable?,
        requiredArguments: List<ArgumentData<*>>)
    : LiteralArgumentBuilder<FabricClientCommandSource?> = if (runnable == null) this
    else this.executes { context ->
        val arguments = mutableListOf<String>()
        for (argument in requiredArguments) {
            arguments.add(context.getArgument(argument.name, argument.argumentClass).toString())
        }

        runnable(arguments.toTypedArray())
        1
    }

    private fun LiteralArgumentBuilder<FabricClientCommandSource?>.registerSubcommands(
        subcommands: Collection<PSSCommand>)
    : LiteralArgumentBuilder<FabricClientCommandSource?> = this.also {
        for (subcommand in subcommands) {
            this.then(literal<FabricClientCommandSource?>(subcommand.name)
                .registerRequiredArguments(subcommand.requiredArguments)
                .registerRunnable(subcommand.runnable, subcommand.requiredArguments)
                .registerSubcommands(subcommand.subcommands),
            )
        }
    }
}
