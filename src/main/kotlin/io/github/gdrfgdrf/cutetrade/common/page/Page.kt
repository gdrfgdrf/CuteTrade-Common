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
import io.github.gdrfgdrf.cutetrade.common.pool.CommonFunctionsPool
import io.github.gdrfgdrf.cutetrade.common.proxy.ItemStackProxy

class Page(
    private val rows: Int
) {
    val slots: ArrayList<ItemStackProxy> = ArrayList(rows * 9)

    fun initialize() {
        for (row in 0 until rows) {
            for (column in 0 until 9) {
                slots.add(ItemStackProxy.EMPTY!!)
            }
        }
    }

    fun show(inventory: Any) {
        val functions =
            CommonFunctionsPool.getFunctions<CommonFunctions.InventoryFunctions>(CommonFunctions.InventoryFunctions::class.java)
        slots.forEachIndexed { index, itemStack ->
            functions.setStack(inventory, index, itemStack)
        }
    }

}