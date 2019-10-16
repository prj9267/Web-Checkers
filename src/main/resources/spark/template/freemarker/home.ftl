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

          <div class="nav">
               <#include "message.ftl" />
               <#if signedIn && username??>
                    <p>You are signed in as ${username}.</p>
               <#else>
                    <p>Not signed in. </p>
                    <p>Sign In Here: <a href="./signin">Sign In</a></P>
               </#if>
          </div>

          <div class="body">
               <#if numPlayers??>
                   <div class="body">
                       <p>Number of Other Users Logged In: #{numPlayers}</p>
                   </div>
               </#if>
               <#if players??>
                   <p>Currently Signed in Players:</p>
                       <#list players as player>
                           <#if signedIn>
                               <#if player.getName() != username>
                                   <ul name="opponent">
                                       <a href="/game">${player.getName()}</a>
                                   </ul>
                               </#if>
                           <#else>
                               <ul name="opponent">
                                   <a href="/game">${player.getName()}</a>
                               </ul>
                           </#if>
                        </#list>
               </#if>
          </div>

            <#-- -------------------------------------------------------------------Below is comment--------
            Provide a navigation bar
            <#include "nav-bar.ftl" />

            <div class="body">
            TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
            <a href="./game">GAME</a>
            ------------------------------------------------------------------------Above is comment -->
      </div>
</body>

</html>
