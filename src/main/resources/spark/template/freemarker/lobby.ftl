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

        <p> Welcome ${playerName} </p>

        <div class="nav">
            <a href="/">Home</a>
        </div>


        <div class="nav">
            <a href="/game">Game</a>
        </div>
        <#if players??>
            <div class="body">
                <p>Currently Signed in Players:</p>
                <#list players as player>
                    <p> <a href="/game">${player.name}</a>
                </#list>
            </div>
        </#if>

    </div>
</body>

</html>