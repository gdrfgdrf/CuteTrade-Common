package io.github.gdrfgdrf.cutetrade.common.command

import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import io.github.gdrfgdrf.cutetrade.common.translation.ConsoleTranslationScopeAgent

object TutorialCommandExecutor {
    fun execute(playerProxy: PlayerProxy?) {
        if (playerProxy != null) {
            playerProxy.translationScope {
                toCommandTranslation("top")
                    .send("")
                toCommandTranslation("tutorial")
                    .send("")
                toCommandTranslation("bottom")
                    .send("")
            }
        } else {
            ConsoleTranslationScopeAgent.apply {
                toCommandTranslation("top")
                    .send("")
                toCommandTranslation("tutorial")
                    .send("")
                toCommandTranslation("bottom")
                    .send("")
            }
        }
    }
}