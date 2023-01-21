/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.models.items.items.game;

import com.wynntils.models.items.properties.UsesItemPropery;
import com.wynntils.utils.type.CappedValue;

public class HealthPotionItem extends GameItem implements UsesItemPropery {
    private final int hearts;
    private final CappedValue uses;

    public HealthPotionItem(int hearts, CappedValue uses) {
        this.hearts = hearts;
        this.uses = uses;
    }

    public int getHearts() {
        return hearts;
    }

    @Override
    public CappedValue getUses() {
        return uses;
    }

    @Override
    public String toString() {
        return "HealthPotionItem{" + "hearts=" + hearts + ", uses=" + uses + '}';
    }
}