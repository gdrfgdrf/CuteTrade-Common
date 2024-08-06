package io.github.gdrfgdrf.cutetrade.common.command.admin

import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import io.github.gdrfgdrf.cutetrade.common.translation.ConsoleTranslationScopeAgent

object HelpAdminCommandExecutor {
    fun execute(playerProxy: PlayerProxy?) {
        if (playerProxy != null) {
            playerProxy.translationScope {
                toCommandTranslation("top")
                    .send("")
                toCommandTranslation("admin_help")
                    .send("")
                toCommandTranslation("bottom")
                    .send("")
            }
        } else {
            ConsoleTranslationScopeAgent.apply {
                toCommandTranslation("top")
                    .send("")
                toCommandTranslation("admin_help")
                    .send("")
                toCommandTranslation("bottom")
                    .send("")
            }
        }
    }
}