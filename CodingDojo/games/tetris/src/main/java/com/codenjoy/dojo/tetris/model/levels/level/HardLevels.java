package com.codenjoy.dojo.tetris.model.levels.level;

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



import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.levels.gamelevel.FigureTypesLevel;
import com.codenjoy.dojo.tetris.model.GlassEvent;
import com.codenjoy.dojo.tetris.model.Levels;

import static com.codenjoy.dojo.tetris.model.Type.*;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.LINES_REMOVED;

public class HardLevels extends Levels {

    public HardLevels(Dice dice, FigureQueue queue) {
        super(new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(LINES_REMOVED, 4),
                        O),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(LINES_REMOVED, 4),
                        O, I),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(LINES_REMOVED, 4),
                        O, I, J, L),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(LINES_REMOVED, 4),
                        O, I, J, L,
                        S, Z, T));
    }
}
