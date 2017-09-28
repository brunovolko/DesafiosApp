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

				//Ahora hay que ver si ya lo habia calificado negativamente
				$consulta = $con->query("SELECT IDCALIFICACION FROM calificaciones WHERE IDUSUARIO='$idusuario' AND IDPUBLICACION='$idPublicacion' AND CALIFICACION='0' LIMIT 1");

				if($consulta->num_rows == 1) {
					echo "Ya lo habia calificado";
					// Ya lo habia calificado negativamente, por lo tanto, hay que sacar esa calificacion
					$con->query("DELETE FROM calificaciones WHERE IDUSUARIO='$idusuario' AND IDPUBLICACION='$idPublicacion' AND CALIFICACION='0' LIMIT 1");
				} else {
					echo "No lo habia calificado";
					//No lo habia calificado, lo califica y por las dudas borro un posible positivo
					$con->query("INSERT INTO calificaciones (IDPUBLICACION, IDUSUARIO, CALIFICACION) VALUES ('$idPublicacion', '$idusuario', '0')");
					
					$con->query("DELETE FROM calificaciones WHERE IDUSUARIO='$idusuario' AND IDPUBLICACION='$idPublicacion' AND CALIFICACION='1' LIMIT 1");

				}

				echo "ok";
				mysqli_close($con);




				
				
				
			} else {
				//Mal
				echo 'error2';
				mysqli_close($con);

			}


			

		} else { echo 'error2'; mysqli_close($con); }
		
	} else { echo 'error2'; }

} else { echo 'error2'; }



?>