package io.github.gdrfgdrf.cutetrade.common.command

import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import io.github.gdrfgdrf.cutetrade.common.translation.ConsoleTranslationScopeAgent

object HelpCommandExecutor {
    fun execute(playerProxy: PlayerProxy?) {
        if (playerProxy != null) {
            playerProxy.translationScope {
                toCommandTranslation("top")
                    .send("")
                toCommandTranslation("help")
                    .send("")
                toCommandTranslation("bottom")
                    .send("")
            }
        } else {
            ConsoleTranslationScopeAgent.apply {
                toCommandTranslation("top")
                    .send("")
                toCommandTranslation("help")
                    .send("")
                toCommandTranslation("bottom")
                    .send("")
            }
        }
    }
}