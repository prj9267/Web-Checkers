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

            <form action="./postsignin" method="POST">
                <input type="text" name="username" placeholder="Username">
                <br>
                <button type="submit">Submit</button>
            </form>

        </div>
    </div>
</body>

</html>
