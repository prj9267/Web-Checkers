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

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <form action="./postsignin" method="POST">
    Username: <input type="text" name="username"><br>
    Password: <input type="text" name="password"><br>
    Confirm Password: <input type="text" name="password"><br>
    <input type="submit" value="Submit">
    </form>

    <!-- TODO: future content on the Sign in:
            username bar,
            password bar,
            confirm password bar
    -->

  </div>

</div>
</body>

</html>
