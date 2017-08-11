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
			$consulta = $con->query("SELECT * FROM usuarios WHERE IDUSUARIO='$_POST[idUsuario]' AND ESTADO='activo' LIMIT 1");

			if($consulta->num_rows == 1) {
				//Todo piolaa
				$usuario = $consulta->fetch_array();

				//Seguidores
				$consulta = $con->query("SELECT count(IDSEGUIMIENTO) as seguidores FROM seguimientos WHERE IDUSUARIOSEGUIDO='$_POST[idUsuario]'");
				$seguidores = $consulta->fetch_array()["seguidores"];

				//Seguidos
				$consulta = $con->query("SELECT count(IDSEGUIMIENTO) as seguidos FROM seguimientos WHERE IDUSUARIOSEGUIDOR='$_POST[idUsuario]'");
				$seguidos = $consulta->fetch_array()["seguidos"];

				//Lo sigue
				$consulta = $con->query("SELECT COUNT(IDSEGUIMIENTO) as RESP FROM seguimientos WHERE IDUSUARIOSEGUIDO='$_POST[idUsuario]' AND IDUSUARIOSEGUIDOR='$idusuario' LIMIT 1");
				$temp = $consulta->fetch_array()["RESP"];
				if($temp == 1) {
					//Lo sigue
					$siguiendo = "1";
				} else {
					// No lo sigue
					$siguiendo = "0";
				}
			

				$arrayDevolver = array(
					"IDUSUARIO" => $usuario["IDUSUARIO"],
					"USUARIO" => $usuario["USUARIO"],
					"TIENEIMAGEN" => $usuario["TIENEIMAGEN"],
					"SEGUIDORES" => $seguidores,
					"SEGUIDOS" => $seguidos,
					"SIGUIENDO" => $siguiendo
				);
				echo json_encode($arrayDevolver, JSON_PRETTY_PRINT);
				mysqli_close($con);

			} else { echo 'error2'; mysqli_close($con); }


			

		} else { echo 'error2'; mysqli_close($con); }
		
	} else { echo 'error2'; }

} else { echo 'error2'; }

?>