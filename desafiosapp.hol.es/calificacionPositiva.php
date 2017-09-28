<?php
if($_SERVER['REQUEST_METHOD']=="POST") {
	require_once("funciones.php");

	if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['idPublicacion']) && is_numeric($_POST['idPublicacion']) && esSeguro($_POST['idPublicacion'])) {
		require_once("conexion.php");

		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe

			$idPublicacion = $_POST['idPublicacion'];

			//Ahora a chekear publicacion a leer
			$consulta = $con->query("SELECT IDPUBLICACION FROM publicaciones WHERE IDPUBLICACION='$idPublicacion' AND ESTADO='activa' LIMIT 1");
			if($consulta->num_rows == 1) {
				//Publicacion ok

				//Ahora hay que ver


				//Borrar
				$consulta=$con->query("DELETE FROM seguimientos WHERE IDUSUARIOSEGUIDOR='$idusuario' AND IDUSUARIOSEGUIDO='$_POST[idUsuario]' LIMIT 1");
				mysqli_close($con);
				
			} else {
				// TOdavia no lo sigo
				//Hay que seguirlo
				//Insertar
				$consulta=$con->query("INSERT INTO seguimientos (IDUSUARIOSEGUIDOR, IDUSUARIOSEGUIDO) VALUES ('$idusuario', '$_POST[idUsuario]')");
				mysqli_close($con);

			}


			

		} else { echo 'error2'; mysqli_close($con); }
		
	} else { echo 'error2'; }

} else { echo 'error2'; }



?>