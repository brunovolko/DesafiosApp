<?php
if($_SERVER['REQUEST_METHOD']=="POST") {
	require_once("funciones.php");

	if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['idUsuario']) && is_numeric($_POST['idUsuario']) && esSeguro($_POST['idUsuario'])) {
		require_once("conexion.php");

		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe

			//Ahora a chekear el usuario a leer
			$consulta = $con->query("SELECT IDSEGUIMIENTO FROM seguimientos WHERE IDUSUARIOSEGUIDOR='$idusuario' AND IDUSUARIOSEGUIDO='$_POST[idUsuario]' LIMIT 1");
			if($consulta->num_rows == 1) {
				//Ya lo sigue
				// Hay que dejar de seguirlo
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