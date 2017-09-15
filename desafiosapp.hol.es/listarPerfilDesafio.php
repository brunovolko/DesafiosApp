<?php
if($_SERVER['REQUEST_METHOD']=="POST") {
	require_once("funciones.php");

	if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['idDesafio']) && is_numeric($_POST['idDesafio']) && esSeguro($_POST['idDesafio'])) {
		require_once("conexion.php");

		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe

			//Ahora a chekear el usuario a leer
			$consulta = $con->query("SELECT IDDESAFIO FROM desafios WHERE IDDESAFIO='$_POST[idDesafio]' LIMIT 1");

			if($consulta->num_rows == 1) {
				//Okk

				$idDesafioALeer = $_POST['idDesafio'];

				$consulta = $con->query("SELECT publicaciones.IDPUBLICACION as IDPUBLICACION, desafios.DESAFIO as DESAFIO, usuarios.IDUSUARIO as IDUSUARIO, usuarios.USUARIO as USUARIO, usuarios.TIENEIMAGEN as TIENEIMAGEN
										FROM (publicaciones
										INNER JOIN usuarios on publicaciones.IDUSUARIO = usuarios.IDUSUARIO)
										INNER JOIN desafios on publicaciones.IDDESAFIO = desafios.IDDESAFIO
										WHERE publicaciones.IDDESAFIO = '$idDesafioALeer' AND publicaciones.ESTADO = 'activa'
										ORDER BY publicaciones.IDPUBLICACION DESC");

				$arrayDevolver = array();
				while($desafio = $consulta->fetch_array()) {

					$idPublicacion = $desafio["IDPUBLICACION"];
					$consultaComentarios = $con->query("SELECT COUNT(IDCOMENTARIO) as CANTIDAD
										FROM comentarios
										WHERE IDPUBLICACION = '$idPublicacion' AND ESTADOCOMENTARIO = 'activo'
										");
					$cantComentarios = $consultaComentarios->fetch_array()["CANTIDAD"];

					$temp = array(
						"IDPUBLICACION" => (int)$desafio["IDPUBLICACION"],
						"DESAFIO" => $desafio["DESAFIO"],
						"IDUSUARIO" => (int)$desafio["IDUSUARIO"],
						"USUARIO" => $desafio["USUARIO"],
						"TIENEIMAGEN" => (int)$desafio["TIENEIMAGEN"],
						"CANTIDADCOMENTARIOS" => $cantComentarios
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