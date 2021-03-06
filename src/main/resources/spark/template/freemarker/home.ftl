<!DOCTYPE html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
        <meta http-equiv="refresh" content="10">
        <title>Web Checkers | ${title}</title>
        <link rel="stylesheet" type="text/css" href="/css/style.css">
    </head>

    <body>
        <div class="page">
            <h1>Web Checkers | ${title}</h1>

            <#include "nav-bar.ftl"/>

            <div class="body">
                <#-- Provide a message to the user, if supplied. -->
                <#include "message.ftl" />
                <#if numPlayers??>
                    <div class="body">
                        <p>Number of Users Logged In:</p>
                            <ul>
                                ${numPlayers}
                            </ul>
                    </div>
                </#if>
            </div>

            <#if currentPlayer??>
                <p> Hello ${currentPlayer}! </p>

                <div class="stats">
                    <h1>Statistics</h1>
                    <p>Games Played: ${games}</p><br>
                    <p>Games Won: ${won}</p><br>
                    <p>Games Lost: ${lost}</p><br>
                    <p>W/L Ratio: ${ratio}</p><br>
                    <p>Opponent Pieces Taken: ${piecesTaken}</p><br>
                    <p>Friendly Pieces Lost: ${piecesLost}</p>
                </div>

                <div class="stats" id="leaderboard">
                    <div id="leaderboard-state">
                        <form action"./">
                            <input type="submit" name="boardButton" value="Games Rankings"></input>
                            <input type="submit" name="boardButton" value="Victory Rankings"></input>
                            <input type="submit" name="boardButton" value="Loss Rankings"></input>
                            <input type="submit" name="boardButton" value="Pieces Taken Rankings"></input>
                            <input type="submit" name="boardButton" value="Pieces Lost Rankings"></input>
                        </form>
                    </div>
                    <div id="rankings">
                        <div id="board-display">
                            <div class="wrapper">
                                <h2 id="username-label" class="label">Username</h2>
                                <#if namesOnly??>
                                    <#list namesOnly as name>
                                        <p id="name_display" class="display">${name}</p>
                                    </#list>
                                </#if>
                            </div>
                            <div class="wrapper">
                                <h2 id="game-label" class="label">Games Played</h2>
                                <#if gamesOnly??>
                                    <#list gamesOnly as game>
                                        <p id="games_display" class="display">${game}</p>
                                    </#list>
                                </#if>
                            </div>
                            <div class="wrapper">
                                <h2 id="won-label" class="label">Games Won</h2>
                                <#if wonOnly??>
                                    <#list wonOnly as won>
                                        <p id="won_display" class="display">${won}</p>
                                    </#list>
                                </#if>
                            </div>
                            <div class="wrapper">
                                <h2 id="lost-label" class="label">Games Lost</h2>
                                <#if lostOnly??>
                                    <#list lostOnly as lost>
                                        <p id="lost_display" class="display">${lost}</p>
                                    </#list>
                                </#if>
                            </div>
                            <div class="wrapper">
                                <h2 id="piecesTaken-label" class="label">Opponent Pieces Taken</h2>
                                <#if piecesTakenOnly??>
                                    <#list piecesTakenOnly as piecesTaken>
                                        <p id="piecesTaken_display" class="display">${piecesTaken}</p>
                                    </#list>
                                </#if>
                            </div>
                            <div class="wrapper">
                                <h2 id="piecesLost-label" class="label">Friendly Pieces Lost</h2>
                                <#if piecesLostOnly??>
                                    <#list piecesLostOnly as piecesLost>
                                        <p id="piecesLost_display" class="display">${piecesLost}</p>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="body">
                    <#if players??>
                        <p>Currently Signed in Players:</p>
                        <form action="./game">
                            <#list players as player>
                                <button type="submit" name=button value="${player.name}"> ${player.name} </button>
                            </#list>
                        </form>
                    </#if>
                </div>


            </#if>
    </body>
</html>
