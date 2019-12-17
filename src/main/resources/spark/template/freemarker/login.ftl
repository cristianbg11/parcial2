<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="css/login.css" rel="stylesheet" type="text/css">
    <title>Login</title>
</head>
<body>
<h2>Sign in/up</h2>
<div class="container" id="container">
    <div class="form-container sign-up-container">
        <form action="/insertar" method="POST">
            <h1>Crear Cuenta</h1>
            <input type="text" placeholder="Usuario" name="username"/>
            <input type="text" placeholder="Nombre" name="nombre"/>
            <input type="password" placeholder="Contrasenia" name="password"/>
            <input type="text" placeholder="Email" name="email"/>
            <input type="text" placeholder="Edad" name="edad"/>
            <input type="hidden" name="administrador" value="FALSE">
            <button>Enviar</button>
        </form>
    </div>
    <div class="form-container sign-in-container">
        <form action="/sesion" method="POST">
            <h1>Iniciar Sesión</h1>
            <input type="text" placeholder="Username" name="user" />
            <input type="password" placeholder="Password" name="pass" />
            <label for="recordatorio" style="display: block; padding-left: 15px; text-indent: -15px;">
                <input type="checkbox" name="recordatorio" id="recordatorio" value="si"
                       style="width: 13px; height: 13px; padding: 0; margin:0; vertical-align: bottom; position: relative; top: -1px; *overflow: hidden;"> Recordar usuario</label>
            <button>Iniciar</button>
            <a href="/visitar"><input type="button" value="Visitar"/></a>
        </form>
    </div>
    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-left">
                <h1>Bienvenido!</h1>
                <p>Para seguir conectado, inicia sesion con tu usuario</p>
                <button class="ghost" id="signIn" >Iniciar Sesión</button>
            </div>
            <div class="overlay-panel overlay-right">
                <h1>Bienvenido!</h1>
                <p>Introduce tus datos personales</p>
                <button class="ghost" id="signUp">Crear Cuenta</button>
            </div>
        </div>
    </div>
</div>

<footer>
</footer>
<script src="js/login.js"></script>
</body>
</html>