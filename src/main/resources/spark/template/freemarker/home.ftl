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

                    <#if leaderboard??>
                        <#list leaderboard as player>
                            <div class="display">
                                <p id="name_display">Name: ${player.name}</p>
                                <p>Number of Games Played: ${player.games}</p>
                                <p>Number of Wins: ${player.won}</p>
                                <p>Number of Losses: ${player.lost}</p>
                                <p>Opponent Pieces Taken: ${player.piecesTaken}</p>
                                <p>Friendly Pieces Lost: ${player.piecesLost}</p>
                            </div>
                        </#list>
                    </#if>
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
