package com.codenjoy.dojo.quadro.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.quadro.model.items.Chip;
import com.codenjoy.dojo.quadro.model.items.RedChip;
import com.codenjoy.dojo.quadro.model.items.YellowChip;
import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.LinkedHashMap;
import java.util.List;

import static com.codenjoy.dojo.games.quadro.Element.RED;
import static com.codenjoy.dojo.games.quadro.Element.YELLOW;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public ChipSet chips() {
        List<Chip> chips = find(new LinkedHashMap<>() {{
            put(YELLOW, YellowChip::new);
            put(RED, RedChip::new);
        }});

        return new ChipSet(){{
            putAll(chips);
        }};
    }
}