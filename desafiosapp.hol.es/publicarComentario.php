<?php
if($_SERVER['REQUEST_METHOD']=="POST"){
	require_once("funciones.php");
	if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['idPublicacion']) && is_numeric($_POST['idPublicacion']) && isset($_POST['comentario']) && !empty(trim($_POST['comentario']))) {

		require_once("conexion.php");

		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe

			$consulta = $con->query("SELECT IDPUBLICACION FROM publicaciones WHERE ESTADO='activa' LIMIT 1");

			if($consulta->num_rows == 1) {
				//Publiacion ok

				$comentario = asegurar(trim($_POST['comentario']));
				$idPublicacion = $_POST['idPublicacion'];
				$query = "
					INSERT INTO comentarios (COMENTARIO, IDPUBLICACION, IDUSUARIO) VALUES ('$comentario', '$idPublicacion','$idusuario')
				";
				if($con->query($query)) {
					echo 'ok';
				} else {
					echo 'error2';
				}

			} else {
				mysqli_close($con);
				echo 'error2';
			}

			
			

		} else { echo 'error2'; mysqli_close($con); }
	} else { echo 'error2'; }
} else { echo 'error2'; }
?>