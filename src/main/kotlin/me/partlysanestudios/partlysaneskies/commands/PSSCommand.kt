//
// Written by Su386.
// See LICENSE for copyright and license notices.
//
package me.partlysanestudios.partlysaneskies.commands

import com.mojang.brigadier.arguments.ArgumentType
import me.partlysanestudios.partlysaneskies.commands.ArgumentData.Companion.Argument
import me.partlysanestudios.partlysaneskies.commands.CommandManager.commandList

class PSSCommand(val name: String) {
    var aliases: MutableList<String> = mutableListOf()
        private set
    var description: String = ""
        private set
    var runnable: PSSCommandRunnable? = null
    val requiredArguments = mutableListOf<ArgumentData<*>>()
    val optionalArguments = mutableListOf<ArgumentData<*>>()
    val subcommands = mutableListOf<PSSCommand>()
    var isRegistered = false

    constructor(
        name: String,
        aliases: MutableList<String> = mutableListOf(),
        description: String,
        runnable: PSSCommandRunnable?,
    ) : this(name, aliases, description) {
        this.runnable = runnable
    }

    constructor(name: String, aliases: MutableList<String>, description: String) : this(name, aliases) {
        this.description = description
    }

    constructor(name: String, aliases: MutableList<String>) : this(name) {
        this.aliases = aliases
    }

    fun addAlias(vararg aliases: String): PSSCommand = this.also { this.aliases.addAll(aliases) }

    fun setRunnable(runnable: PSSCommandRunnable?): PSSCommand = this.also { this.runnable = runnable }

    fun setDescription(description: String): PSSCommand = this.also { this.description = description }

    inline fun <reified T> addArgument(name: String, argumentType: ArgumentType<T>): PSSCommand = this.also { requiredArguments.add(Argument(name, argumentType)) }

    fun addSubcommands(vararg subcommands: PSSCommand): PSSCommand = this.also { this.subcommands.addAll(subcommands) }

    fun runRunnable(args: Array<String>) = runnable?.let { it(args) }

    fun register() {
        commandList[this.name]?.add(this) ?: (mutableSetOf(this).also { commandList[this.name] = it })
    }
}

typealias PSSCommandRunnable = (Array<String>) -> Unit
data class ArgumentData<T>(
    val name: String,
    val type: ArgumentType<T>,
    val argumentClass: Class<T>
) {
    companion object {
        inline fun <reified T> Argument(name: String, type: ArgumentType<T>): ArgumentData<T> = ArgumentData(name, type, T::class.java)
    }
}
