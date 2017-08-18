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
			$consulta = $con->query("SELECT IDUSUARIO FROM usuarios WHERE IDUSUARIO='$_POST[idUsuario]' AND ESTADO='activo' LIMIT 1");

			if($consulta->num_rows == 1) {
				//Okk

				$idUsuarioALeer = $_POST['idUsuario'];

				$consulta = $con->query("SELECT publicaciones.IDPUBLICACION as IDPUBLICACION, desafios.DESAFIO as DESAFIO, usuarios.IDUSUARIO as IDUSUARIO, usuarios.USUARIO as USUARIO, usuarios.TIENEIMAGEN as TIENEIMAGEN
										FROM (publicaciones
										INNER JOIN usuarios on publicaciones.IDUSUARIO = usuarios.IDUSUARIO)
										INNER JOIN desafios on publicaciones.IDDESAFIO = desafios.IDDESAFIO
										WHERE publicaciones.IDUSUARIO = '$idUsuarioALeer' AND publicaciones.ESTADO = 'activa'
										ORDER BY publicaciones.IDPUBLICACION DESC");

				$arrayDevolver = array();
				while($desafio = $consulta->fetch_array()) {

					$temp = array(
						"IDPUBLICACION" => $desafio["IDPUBLICACION"],
						"DESAFIO" => $desafio["DESAFIO"],
						"IDUSUARIO" => $desafio["IDUSUARIO"],
						"USUARIO" => $desafio["USUARIO"],
						"TIENEIMAGEN" => $desafio["TIENEIMAGEN"]
					);
					$arrayDevolver[] = $temp;
				}
				echo json_encode($arrayDevolver, JSON_PRETTY_PRINT);

				
				mysqli_close($con);

			} else { echo 'error2'; mysqli_close($con); }


			

		} else { echo 'error2'; mysqli_close($con); }
		
	} else { echo 'error2'; }

} else { echo 'error2'; }

?>