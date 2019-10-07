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

        <div class="body">
            <div class="nav">
                <a href="/">Home</a>
            </div>

            <!-- Provide a message to the user, if supplied. -->
            <#include "message.ftl" />

            <form action="./home" method="POST">
                <input type="text" name="username" placeholder="Username">
                <br>

                <!-- TODO: removing since unsure if needed for now and to make things simpler - MIKE
                Password: <input type="text" name="password" method="POST"><br>
                ----------------------------------------------------------------------- -->

                <br>
                <button type="submit">Submit Information</button>
            </form>

            <!-- TODO: future content on the Sign Up:
                username bar,
                password bar
            -->

        </div>
    </div>
</body>