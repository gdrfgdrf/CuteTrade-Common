package io.github.cutetrade.common.translation

import io.github.cutetrade.common.CommonFunctions
import io.github.cutetrade.common.pool.CommonFunctionsPool
import io.github.cutetrade.common.proxy.ItemStackProxy
import io.github.cutetrade.common.proxy.TextProxy

class TranslationTextAgent private constructor(
    val raw: String,
    var cuteText: Any
) {
    var finalString: String = raw

    var clickAction: ClickTextAction? = null
    var clickActionValue: Any? = null

    var hoverAction: HoverTextAction? = null
    var hoverActionValue: Any? = null

    private fun clickAction(clickTextAction: ClickTextAction): TranslationTextAgent {
        this.clickAction = clickTextAction
        return this
    }

    private fun clickActionValue(any: Any): TranslationTextAgent {
        if (this.clickAction == null) {
            return this
        }
        this.clickActionValue = any
        return this
    }

    fun openUrl(url: String): TranslationTextAgent {
        clickAction(ClickTextAction.OPEN_URL)
        clickActionValue(url)
        return this
    }

    fun openFile(file: String): TranslationTextAgent {
        clickAction(ClickTextAction.OPEN_FILE)
        clickActionValue(file)
        return this
    }

    fun runCommand(command: String): TranslationTextAgent {
        clickAction(ClickTextAction.RUN_COMMAND)
        clickActionValue(command)
        return this
    }

    fun suggestCommand(command: String): TranslationTextAgent {
        clickAction(ClickTextAction.SUGGEST_COMMAND)
        clickActionValue(command)
        return this
    }

    fun changePage(value: String): TranslationTextAgent {
        clickAction(ClickTextAction.CHANGE_PAGE)
        clickActionValue(value)
        return this
    }

    fun copyToClipboard(value: String): TranslationTextAgent {
        clickAction(ClickTextAction.COPY_TO_CLIPBOARD)
        clickActionValue(value)
        return this
    }

    private fun hoverAction(hoverTextAction: HoverTextAction): TranslationTextAgent {
        this.hoverAction = hoverTextAction
        return this
    }

    private fun hoverActionValue(any: Any): TranslationTextAgent {
        if (this.hoverAction == null) {
            return this
        }
        val proxy =
            CommonFunctionsPool.getFunctions<CommonFunctions.TranslationTextFunctions>(
                CommonFunctions.TranslationTextFunctions::class.java
            )
        val value = when (this.hoverAction) {
            HoverTextAction.SHOW_TEXT -> proxy.createShowText(any as String)
            HoverTextAction.SHOW_ITEM -> proxy.createShowItem(any as ItemStackProxy)
//            HoverTextAction.SHOW_ENTITY -> proxy.createShowEntity(any)
            HoverTextAction.SHOW_ENTITY -> proxy.createShowText(any.toString())
            null -> TODO()
        }
        this.hoverActionValue = value
        return this
    }

    fun showText(value: String): TranslationTextAgent {
        hoverAction(HoverTextAction.SHOW_TEXT)
        hoverActionValue(value)
        return this
    }

    fun showItem(itemStackProxy: ItemStackProxy): TranslationTextAgent {
        hoverAction(HoverTextAction.SHOW_ITEM)
        hoverActionValue(itemStackProxy)
        return this
    }

    fun showEntity(any: Any): TranslationTextAgent {
        hoverAction(HoverTextAction.SHOW_ENTITY)
        hoverActionValue(any)
        return this
    }

    fun format(vararg any: Any): TranslationTextAgent {
        finalString = finalString.format(any)
        return this
    }

    fun build(): TextProxy {
        val buildFunction = CommonFunctionsPool.getFunctions<CommonFunctions.TranslationTextFunctions>(
            CommonFunctions.TranslationTextFunctions::class.java
        )
        return buildFunction.build(this)
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