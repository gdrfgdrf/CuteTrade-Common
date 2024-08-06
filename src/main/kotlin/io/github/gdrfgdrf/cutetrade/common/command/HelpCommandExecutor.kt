package io.github.gdrfgdrf.cutetrade.common.command

import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

object HelpCommandExecutor {
    fun execute(playerProxy: PlayerProxy) {
        playerProxy.translationScope {
            toCommandTranslation("top")
                .send("")
            toCommandTranslation("help")
                .send("")
            toCommandTranslation("bottom")
                .send("")
        }
    }
}