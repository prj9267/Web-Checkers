 <!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>

    <div class="navigation">
        <#if currentplayer??>
             <a href="/">my home</a> |
            <form id="signout" action="/signout" method="post">
                TODO
                <a href="#" onclick="event.preventDefault(); signout.submit();">sign out [${currentUser.name}]</a>
            </form>
        <#else>
            <a href="./signin">Sign In</a>
        </#if>
    </div>

</body>

</html>
