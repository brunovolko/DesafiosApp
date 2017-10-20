<?php
if($_SERVER['REQUEST_METHOD']=="POST") {
	require_once("funciones.php");

	if(isset($_POST["token"]) && esSeguro($_POST["token"])) {
		require_once("conexion.php");

		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];


			$consultaDatos = $con->query("SELECT USUARIO, TIENEIMAGEN FROM usuarios WHERE IDUSUARIO='$idusuario' LIMIT 1");


			$datos = $consultaDatos->fetch_array();
			$usuario = $datos["USUARIO"];
			$tieneImagen = $datos["TIENEIMAGEN"];
			// Esta logueado como un campeon, existe




			//Seguidores
			$consulta = $con->query("SELECT count(IDSEGUIMIENTO) as seguidores FROM seguimientos WHERE IDUSUARIOSEGUIDO='$idusuario'");
			$seguidores = $consulta->fetch_array()["seguidores"];

			//Seguidos
			$consulta = $con->query("SELECT count(IDSEGUIMIENTO) as seguidos FROM seguimientos WHERE IDUSUARIOSEGUIDOR='$idusuario'");
			$seguidos = $consulta->fetch_array()["seguidos"];


			$arrayDevolver = array(
				"IDUSUARIO" => $idusuario,
				"USUARIO" => $usuario,
				"TIENEIMAGEN" => $tieneImagen,
				"SEGUIDORES" => $seguidores,
				"SEGUIDOS" => $seguidos
			);
			echo json_encode($arrayDevolver, JSON_PRETTY_PRINT);
			mysqli_close($con);



			

		} else { echo 'error2'; mysqli_close($con); }
		
	} else { echo 'error2'; }

} else { echo 'error2'; }

?>