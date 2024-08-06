package io.github.cutetrade.common.translation

import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.pool.CommonFunctionsPool
import io.github.cutetrade.common.proxy.ItemStackProxy
import io.github.cutetrade.common.proxy.TextProxy

class TranslationTextAgent(
    var cuteText: Any
) {
    private val functions = CommonFunctionsPool.getFunctions<CommonFunctions.TranslationTextFunctions>(
        CommonFunctions.TranslationTextFunctions::class.java
    )

    private fun clickAction(clickTextAction: ClickTextAction, any: Any): TranslationTextAgent {
        functions.clickAction(this, clickTextAction, any)
        return this
    }

    fun openUrl(url: String): TranslationTextAgent {
        clickAction(ClickTextAction.OPEN_URL, url)
        return this
    }

    fun openFile(file: String): TranslationTextAgent {
        clickAction(ClickTextAction.OPEN_FILE, file)
        return this
    }

    fun runCommand(command: String): TranslationTextAgent {
        clickAction(ClickTextAction.RUN_COMMAND, command)
        return this
    }

    fun suggestCommand(command: String): TranslationTextAgent {
        clickAction(ClickTextAction.SUGGEST_COMMAND, command)
        return this
    }

    fun changePage(value: String): TranslationTextAgent {
        clickAction(ClickTextAction.CHANGE_PAGE, value)
        return this
    }

    fun copyToClipboard(value: String): TranslationTextAgent {
        clickAction(ClickTextAction.COPY_TO_CLIPBOARD, value)
        return this
    }

    private fun hoverAction(hoverTextAction: HoverTextAction, any: Any): TranslationTextAgent {
        val proxy =
            CommonFunctionsPool.getFunctions<CommonFunctions.TranslationTextFunctions>(
                CommonFunctions.TranslationTextFunctions::class.java
            )
        val value = when (hoverTextAction) {
            HoverTextAction.SHOW_TEXT -> proxy.createShowText(any as String)
            HoverTextAction.SHOW_ITEM -> proxy.createShowItem(any as ItemStackProxy)
//            HoverTextAction.SHOW_ENTITY -> proxy.createShowEntity(any)
            HoverTextAction.SHOW_ENTITY -> proxy.createShowText(any.toString())
            null -> TODO()
        }
        functions.hoverAction(this, hoverTextAction, value)

        return this
    }

    fun showText(value: String): TranslationTextAgent {
        hoverAction(HoverTextAction.SHOW_TEXT, value)
        return this
    }

    fun showItem(itemStackProxy: ItemStackProxy): TranslationTextAgent {
        hoverAction(HoverTextAction.SHOW_ITEM, itemStackProxy)
        return this
    }

    fun showEntity(any: Any): TranslationTextAgent {
        hoverAction(HoverTextAction.SHOW_ENTITY, any)
        return this
    }

    fun format(vararg any: Any): TranslationTextAgent {
        functions.format(this, *any)
        return this
    }

    fun build(): TextProxy {
        return functions.build(this)
    }

    companion object {
        fun of(raw: String): TranslationTextAgent {
            val functions = CommonFunctionsPool.getFunctions<CommonFunctions.TranslationTextFunctions>(
                CommonFunctions.TranslationTextFunctions::class.java
            )
            val instance = functions.create(raw)
            return instance
        }
    }
}