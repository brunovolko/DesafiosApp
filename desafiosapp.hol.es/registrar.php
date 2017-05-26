<?php 


	if($_SERVER["REQUEST_METHOD"] == "post") {
		if(isset($_POST['usuario']) && !empty($_POST['usuario'])) {
			require_once("funciones.php");
			if(isset($_POST['email']) && !empty($_POST['email']) && esEmail($_POST['email'])) {
				if(isset($_POST['password']) && !empty($_POST['password']) && strlen($_POST['password']) > 6) {
					if(isset($_POST['password2']) && $_POST['password'] == $_POST['password2']) {
						require_once("conexion.php");
						$usuario = asegurar(trim($_POST['usuario']));
						$email = asegurar(trim($_POST['email']));
						$consultaUsuarios = $con->query("SELECT USUARIO FROM usuarios WHERE USUARIO='$usuario' LIMIT 1");
						if($consultaUsuarios->num_rows == 0) {
							$consultaEmails = $con->query("SELECT EMAIL FROM usuarios WHERE EMAIL='$email' LIMIT 1");
							if($consultaEmails->num_rows == 0) {

								// El usuario esta joya

								require_once "PasswordHash.php";

								// Creamos el objeto que nos permitirá gestionar nuestro hash
								$hasher = new PasswordHash(8, FALSE);

								// Generamos el hash
								$nuevaClave = $hasher->HashPassword($_POST['password']);


								$caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"; //posibles caracteres a usar
								$numerodeletras=20; //numero de letras para generar el texto
								$hash = ""; //variable para almacenar la cadena generada
								for($i=0;$i<$numerodeletras;$i++)
								{
								    $hash .= substr($caracteres,rand(0,strlen($caracteres)),1); // Extraemos 1 caracter de los caracteres entre el rango 0 a Numero de letras que tiene la cadena 
								}

								$con->query("INSERT INTO usuarios (USUARIO, EMAIL, CONTRASENA, ESTADO) VALUES ('$usuario', '$email', '$nuevaClave', 'pendiente')");



								$headers .= "From: AltaApp <info@altaapp.com> \r\n";
								$asunto = "Gracias por registrarse en esta app pedorra";
								$mensaje = '

								<html>
								<body>

								<h3>Confirmá la cuenta, no te ortives!<br>
								<b>Hace clic <a title="Confirmar cuenta" href="http://proyectoinfo.hol.es/confirmar/?hash='.$hash.'">aca</a></b></h3>

								</body>
								</html>';



								mail(trim($_POST['email']), $asunto, $mensaje, $headers));


								
							} else { http_response_code(400); echo "6"; }
						} else { http_response_code(400); echo "5"; }
					} else { http_response_code(400); echo "4"; }
				} else { http_response_code(400); echo "3"; }
			} else { http_response_code(400); echo "2"; }
		} else { http_response_code(400); echo "1"; }
	} else { http_response_code(400); }
?>