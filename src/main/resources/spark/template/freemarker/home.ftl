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
                <div class="body">
                    <#if players??>
                        <p>Currently Signed in Players:</p>
                        <#list players as player>
                            <p> <a href="/game">${player}</a>
                        </#list>
                    </#if>
                </div>
            </#if>
    </body>
</html>
