package com.codenjoy.dojo.spacerace.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.games.spacerace.Element;

/**
 * Артефакт Бомба на поле
 */
public class Bomb extends PointImpl implements State<Element, Player>, Tickable {
    private Direction direction;

    public Bomb(int x, int y) {
        super(x, y);
        direction = Direction.DOWN;
    }

    public Bomb(Point point) {
        super(point);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return Element.BOMB;
    }

    @Override
    public void tick() {
        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);
            move(newX, newY);
        }
    }
}
