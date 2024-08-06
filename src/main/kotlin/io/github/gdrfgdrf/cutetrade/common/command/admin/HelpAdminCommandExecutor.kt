package io.github.gdrfgdrf.cutetrade.common.command.admin

import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy

object HelpAdminCommandExecutor {
    fun execute(playerProxy: PlayerProxy) {
        playerProxy.translationScope {
            toCommandTranslation("top")
                .send("")
            toCommandTranslation("admin_help")
                .send("")
            toCommandTranslation("bottom")
                .send("")
        }
    }
}