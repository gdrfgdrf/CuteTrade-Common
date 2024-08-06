package io.github.gdrfgdrf.cutetrade.common.command

import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

object TutorialCommandExecutor {
    fun execute(playerProxy: PlayerProxy) {
        playerProxy.translationScope {
            toCommandTranslation("top")
                .send("")
            toCommandTranslation("tutorial")
                .send("")
            toCommandTranslation("bottom")
                .send("")
        }
    }
}