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

// ========================== game setup ==========================

if (typeof setup == 'undefined') {
    setup = {};
    setup.demo = true;
    setup.code = 123;
    setup.playerId = 'userId';
    setup.readableName = 'Stiven Pupkin';
    initLayout = function(game, html, context, transformations, scripts, onLoad) {
        onLoad();
    }
}

setup.enablePlayerInfo = false;
setup.enablePlayerInfoLevel = false;
setup.showBody = false;

// ========================== leaderboard page ==========================

var boardAllPageLoad = function() {
    // https://github.com/uxitten/polyfill/blob/master/string.polyfill.js
    // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/padStart
    if (!String.prototype.padStart) {
        String.prototype.padStart = function padStart(targetLength,padString) {
            targetLength = targetLength>>0; //floor if number or convert non-number to 0;
            padString = String(padString || ' ');
            if (this.length > targetLength) {
                return String(this);
            }
            else {
                targetLength = targetLength-this.length;
                if (targetLength > padString.length) {
                    padString += padString.repeat(targetLength/padString.length); //append to original to ensure we are longer than needed
                }
                return padString.slice(0,targetLength) + String(this);
            }
        };
    }

    // https://stackoverflow.com/a/32754249
    String.prototype.splitAll = function() {
        var target = this;
        var delimiters = [];
        for (var i = 0; i < arguments.length; i++) {
            delimiters.push(arguments[i]);
        }

        var result = [target];
        while (delimiters.length > 0) {
            for (var i = 0; i < result.length; i++) {
                var tempSplit = result[i].split(delimiters[0]);
                result = result.slice(0, i)
                    .concat(tempSplit)
                    .concat(result.slice(i + 1));
            }
            delimiters.shift();
        }
        return result;
    }

    initLeadersTable(setup.contextPath, setup.playerId, setup.code,
        function(count, you, link, name, team, score) {
            var star = '';
            if (count == 1) {
                star = 'first';
            } else if (count <= 3) {
                star = 'second';
            }

            // TODO мы снова все парсим, чтобы разложить по div со стилями,
            //    быть может лучше в json передавать в разных полях составляющие?
            var details = score.details;
            var parts = details.splitAll('%(∑', ')[i', ']');
            var averageString = parts[0];
            var scoreAmount = parts[1];
            var roundCount = parts[2];
            var rounds = parts[3];

            var l1 = 10;
            var l2 = l1 + 20;
            var rounds1 = rounds.substring(0, l1);
            var dots = '...';
            if (rounds.length > l1) {
                var rounds2 = rounds.substring(l1, l2);
                if (rounds.length > l2) {
                    rounds2 = rounds2 + dots;
                }
            } else {
                rounds2 = '';
            }

            return '<tr>' +
                    '<td><span class="' + star + ' star">' + count + '<span></td>' +
                    '<td>' + '<a href="' + link + '">' + name + '</a>' +
                        '<span class="team-pow">' + team + '</span>' +
                        '<span>' + you + '</span>' +
                    '</td>' +
                    '<td class="left">' +
                        '<span>' + averageString + '<span class="small-round">%</span>' +
                        '(∑' + scoreAmount + ')</span>' +
                        '<span class="small-round">' + '[i' + roundCount + ']' + rounds1 + '</span>' +
                        '<span class="smaller-round">' + rounds2 + '</span>' +
                    '</td>' +
                '</tr>';
        },
        function(score) {
            var scoreAmount = score.score;
            var roundCount = score.rounds.length;
            var average = (roundCount == 0) ? 0 : (scoreAmount/roundCount);
            return average;
        });
    $('#table-logs').removeClass('table');
    $('#table-logs').removeClass('table-striped');
    $(document.body).show();
}

// overrides the method in the canvases-expansion.js
var previous = setup.onBoardPageLoad;
setup.onBoardPageLoad = function() {
    setup.onBoardAllPageLoad();
}

setup.onBoardAllPageLoad = function() {
    previous();
    boardAllPageLoad();

    $('#help-link').attr('href', 'https://docs.google.com/document/d/1SPvBsZKtkk7F28sLtuUo2kOFtNWIz_8umWYYYLZ7kWY/edit')
}

// ========================== demo stuff ==========================

if (setup.demo) {
    setup.onBoardPageLoad();
}