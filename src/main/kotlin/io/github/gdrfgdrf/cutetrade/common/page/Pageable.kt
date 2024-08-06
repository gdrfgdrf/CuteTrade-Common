/*
 * Copyright 2024 CuteTrade's contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.gdrfgdrf.cutetrade.common.page

import io.github.gdrfgdrf.cutetrade.common.CommonFunctions
import io.github.gdrfgdrf.cutetrade.common.extension.translationScope
import io.github.gdrfgdrf.cutetrade.common.pool.CommonFunctionsPool
import io.github.gdrfgdrf.cutetrade.common.proxy.ItemStackProxy
import io.github.gdrfgdrf.cutetrade.common.proxy.PlayerProxy
import io.github.gdrfgdrf.cutetrade.common.proxy.TextProxy
import io.github.gdrfgdrf.cutetrade.common.trade.screen.handler.ScreenHandlerAgent
import io.github.gdrfgdrf.cutetrade.common.translation.TranslationTextAgent

class Pageable {
    var pageableScreenHandler: ScreenHandlerAgent? = null

    var inventory: Any? = null
    private var latestPageIndex: Int = 0
    private var itemCount = 0

    var navigator: Navigator? = null

    fun openScreen(displayName: TextProxy, serverPlayerEntity: PlayerProxy) {
        val factoryGetter =
            CommonFunctionsPool.getFunctions<CommonFunctions.PageableScreenHandlerFactoryGetter>(
                CommonFunctions.PageableScreenHandlerFactoryGetter::class.java
            )

        val factory = factoryGetter.create(displayName)
        serverPlayerEntity.openHandledScreen(factory)
        pageableScreenHandler = serverPlayerEntity.currentScreenHandler()
        inventory = pageableScreenHandler!!.getInventory()

        if (inventory != null) {
            val pageableInventoryFunctions = CommonFunctionsPool.getFunctions<CommonFunctions.PageableInventoryFunctions>(
                CommonFunctions.PageableInventoryFunctions::class.java
            )
            pageableInventoryFunctions.setPageable(inventory!!, this)
        }
    }

    fun addItemStack(itemStack: ItemStackProxy) {
        if (itemCount == 53) {
            itemCount = 0
            latestPageIndex++
        }
        val pageSize = navigator?.pages?.size
        if (latestPageIndex >= pageSize!!) {
            addPage()
        }

        val page = navigator?.pages?.get(latestPageIndex)

        page!!.slots[itemCount] = itemStack
        itemCount++
    }

    fun addPage() {
        val page = Page(6)
        page.initialize()
        navigator?.pages?.add(page)
    }

    fun setOnItemClick(onItemClick: (Int) -> Unit) {
        val functions =
            CommonFunctionsPool.getFunctions<CommonFunctions.PageableScreenHandlerFunctions>(CommonFunctions.PageableScreenHandlerFunctions::class.java)
        functions.setOnItemClick(pageableScreenHandler!!, onItemClick)
    }

    fun fullNavigationBar(serverPlayerEntity: PlayerProxy) {
        val pages = navigator?.pages
        val first = pages?.get(0)
        val last = pages?.get(navigator?.pages?.size!! - 1)

        if (first == last) {
            addNavigationBar(first!!, left = false, right = false, serverPlayerEntity)
            return
        } else {
            addNavigationBar(first!!, left = false, right = true, serverPlayerEntity)
            addNavigationBar(last!!, left = true, right = false, serverPlayerEntity)
        }

        pages.forEach {
            if (it == first || it == last) {
                return@forEach
            }

            addNavigationBar(it, left = true, right = true, serverPlayerEntity)
        }
    }

    private fun addNavigationBar(page: Page, left: Boolean, right: Boolean, serverPlayerEntity: PlayerProxy) {
        serverPlayerEntity.translationScope {
            val itemFunctions =
                CommonFunctionsPool.getFunctions<CommonFunctions.ItemFunctions>(CommonFunctions.ItemFunctions::class.java)
            val itemStackFunctions =
                CommonFunctionsPool.getFunctions<CommonFunctions.ItemStackFunctions>(CommonFunctions.ItemStackFunctions::class.java)

            val previous = itemStackFunctions.create(itemFunctions.get("LIME_WOOL"))
            previous.setCustomName(toScreenText("previous_page").build())

            val next = itemStackFunctions.create(itemFunctions.get("LIME_WOOL"))
            next.setCustomName(toScreenText("next_page").build())

            val redPane = itemStackFunctions.create(itemFunctions.get("RED_STAINED_GLASS_PANE"))
            redPane.setCustomName(toScreenText("close").build())

            val whilePane = itemStackFunctions.create(itemFunctions.get("WHILE_STAINED_CLASS_PANE"))
            whilePane.setCustomName(TranslationTextAgent.of("").build())

            if (!left && !right) {
                for (i in 0 until 4) {
                    page.slots[45 + i] = whilePane
                }

                page.slots[49] = redPane

                for (i in 0 until 4) {
                    page.slots[49 + 1 + i] = whilePane
                }
                return@translationScope
            }
            if (left && right) {
                page.slots[45] = previous

                for (i in 0 until 3) {
                    page.slots[45 + 1 + i] = whilePane
                }

                page.slots[49] = redPane

                for (i in 0 until 3) {
                    page.slots[49 + 1 + i] = whilePane
                }

                page.slots[53] = next
                return@translationScope
            }
            if (!left) {
                for (i in 0 until 4) {
                    page.slots[45 + i] = whilePane
                }

                page.slots[49] = redPane

                for (i in 0 until 3) {
                    page.slots[49 + 1 + i] = whilePane
                }

                page.slots[53] = next
                return@translationScope
            }
            page.slots[45] = previous

            for (i in 0 until 3) {
                page.slots[45 + 1 + i] = whilePane
            }

            page.slots[49] = redPane

            for (i in 0 until 4) {
                page.slots[49 + 1 + i] = whilePane
            }
        }
    }
}