<?php
if($_SERVER['REQUEST_METHOD']=="POST") {
	require_once("funciones.php");

	if(isset($_POST["token"]) && esSeguro($_POST["token"])) {
		require_once("conexion.php");

		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe



			$consulta = $con->query("SELECT publicaciones.IDPUBLICACION as IDPUBLICACION, desafios.DESAFIO as DESAFIO, usuarios.IDUSUARIO as IDUSUARIO, usuarios.USUARIO as USUARIO, usuarios.TIENEIMAGEN as TIENEIMAGEN
										FROM ((publicaciones
										INNER JOIN seguimientos on publicaciones.IDUSUARIO = seguimientos.IDUSUARIOSEGUIDO)
										INNER JOIN usuarios on publicaciones.IDUSUARIO = usuarios.IDUSUARIO)
										INNER JOIN desafios on publicaciones.IDDESAFIO = desafios.IDDESAFIO
										WHERE seguimientos.IDUSUARIOSEGUIDOR = '$idusuario' AND usuarios.ESTADO = 'activo' AND publicaciones.ESTADO = 'activa' ");

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
		
	} else { echo 'error2'; }

} else { echo 'error2'; }

?>