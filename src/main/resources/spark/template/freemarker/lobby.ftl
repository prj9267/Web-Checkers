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
        <#--<#if lobby??>
            <div class="body">
                <p>Choose Your Opponent:</p>
                <ul>
                    <#list lobby as player>
                        <a href="/Game?opponentName=${player}&username=${username}">
                            <li>${player}</li>
                        </a>
                    </#list>
                </ul>
            </div>
        </#if>-->

    </div>
</body>

</html>