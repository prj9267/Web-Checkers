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

          <#if loggedIn == 1>
               <div class="nav">
                     Already a User? <a href="./signin">Sign In</a>
               </div>

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
          <#else>
              <p> Hello ${playerName}! </p>
              <div class="body">
                  <#include "message.ftl" />
                  <#if players??>
                        <p>Currently Signed in Players:</p>
                        <#list players as player>
                        <p> <a href="/game">${player}</a>
                        </#list>
                  </#if>
          </#if>





            <#-- -------------------------------------------------------------------Below is comment--------
            Provide a navigation bar
            <#include "nav-bar.ftl" />

            <div class="body">



                  <#-- TODO: future content on the Home:
                  to start games,
                  spectating active games,
                  or replay archived games


                  <a href="./game">GAME</a>-->

            </div>

      </div>
</body>

</html>
