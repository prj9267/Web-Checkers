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
                  Already a User? <a href="./signin">Sign In</a>
            </div>

            <div class="nav">
                  Don't Have an Account? <a href="./signup">Sign Up</a>
            </div>

            <div class="body">
                <#-- Provide a message to the user, if supplied. -->
                              <#include "message.ftl" />
            </div>


            <#-- -------------------------------------------------------------------Below is comment--------
            Provide a navigation bar
            <#include "nav-bar.ftl" />-->

            <#--<div class="body">



                  <#-- TODO: future content on the Home:
                  to start games,
                  spectating active games,
                  or replay archived games
                  -->

                  <a href="./game">GAME</a>

            </div>-- --------------------------------------------------------------------Above is comment-- -->

      </div>
</body>

</html>
