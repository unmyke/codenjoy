package com.codenjoy.dojo.services.multiplayer;

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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_MAX_TEAMS_PER_ROOM;

public class GameRoom {

    private final GameField field;
    private final int count;
    private int wasCount;
    private final boolean disposable;
    private List<GamePlayer> players = new LinkedList<>();

    public GameRoom(GameField field, int count, boolean disposable) {
        this.field = field;
        this.count = count;
        this.disposable = disposable;
    }

    public GameField join(GamePlayer player) {
        if (!containsPlayer(player)) {
            wasCount++;
            players.add(player);
        }
        return field;
    }

    public boolean isAvailable(GamePlayer player) {
        if (!isFree()) {
            return false;
        }
        if (player.settings != null
                && player.settings.hasParameter(ROUNDS_MAX_TEAMS_PER_ROOM.key()))
        {
            int maxTeams = player.settings.integer(ROUNDS_MAX_TEAMS_PER_ROOM);
            if (!containsTeam(player.getTeamId()) && countTeams() >= maxTeams) {
                return false;
            }
            if (wasCount + 1 == count) {
                return true;
            }
            if (countTeams() > 1 && countMembers(player.getTeamId()) >= count / maxTeams) {
                return false;
            }
        }
        return true;
    }

    public boolean isFree() {
        if (disposable) {
            return wasCount < count;
        } else {
            return countPlayers() < count;
        }
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean isStuffed() {
        if (disposable) {
            return wasCount == count;
        } else {
            return true;
        }
    }

    public boolean containsPlayer(GamePlayer player) {
        return players.contains(player);
    }

    public boolean containsTeam(int teamId) {
        return players.stream()
                .anyMatch(p -> p.getTeamId() == teamId);
    }

    public long countTeams() {
        return players.stream()
                .map(GamePlayer::getTeamId)
                .distinct()
                .count();
    }

    public long countPlayers() {
        return players.size();
    }

    public long countMembers(int teamId) {
        return players.stream()
                .filter(p -> p.getTeamId() == teamId)
                .distinct()
                .count();
    }

    public boolean isFor(GameField input) {
        if (field == null) { // TODO точно такое может быть?
            return input == null;
        }
        return field.equals(input);
    }

    public Collection<GamePlayer> players() {
        return players;
    }

    /**
     * @param player Игрок который закончил играть в этой room и будет удален
     * @return Все игроки этой комнаты, которых так же надо пристроить в новой room,
     * т.к. им тут оставаться нет смысла
     */
    public List<GamePlayer> remove(GamePlayer player) {
        List<GamePlayer> removed = new LinkedList<>();

        players.remove(player);

        if (players.size() == 1) { // TODO ##1 тут может не надо выходить если тип игры MULTIPLAYER
            GamePlayer last = players.iterator().next();
            if (!last.wantToStay()) {
                removed.add(last);
                players.remove(last);
            }
        }

        return removed;
    }

}
