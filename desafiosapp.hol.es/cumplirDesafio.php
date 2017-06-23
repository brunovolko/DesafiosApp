<?php
if($_SERVER['REQUEST_METHOD']=="POST"){
	require_once("funciones.php");
	if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['desafio']) && is_numeric($_POST['desafio']) && isset($_FILES['imagen']) && $_FILES["imagen"]["type"] == "image/jpg") {
		require_once("conexion.php");
		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe

			require_once("../forbidden/Resizer.php");

			$dirTemp = $_FILES["imagen"]["tmp_name"]; // Image selected in a form's input. Only works with .jpg and .png images.
			$consultaUltimaPublicacion = $con->query("SELECT IDPUBLICACION FROM publicaciones ORDER BY IDPUBLICACION DESC LIMIT 1");
			$ultimaPublicacion = $consultaUltimaPublicacion->fetch_array()["IDPUBLICACION"] + 1;
			$newImageName = $ultimaPublicacion; // Name of rezised image.
			$destiny = "imagenes/publicaciones/"; // Destiny directory where the image will be saved.
			// You have to set the maximum width and height, so the image can be resized proportionally and without exceeding the chosen limits.
			$wishedWidth = 700;
			$wishedHeight = 700;
			$format = ".jpg"; // Wished format (Only .jpg or .png)
			if(redimensionar($dirTemp, $newImageName, $destiny, $wishedWidth, $wishedHeight, $format))
			{
				//ok
				$query = "
				SELECT desafios.IDDESAFIO as IDDESAFIO
				FROM (desafios
				INNER JOIN usuarios on desafios.IDUSUARIO = usuarios.IDUSUARIO)
				INNER JOIN seguimientos on usuarios.IDUSUARIO = seguimientos.IDUSUARIOSEGUIDO
				WHERE desafios.IDDESAFIO = '$_POST[desafio]' AND seguimientos.IDUSUARIOSEGUIDOR = '$idusuario'
				LIMIT 1
					";
				$consultaDesafioOk = $con->query($query);

				echo $con->error;


				if($consultaDesafioOk->num_rows == 1) {
					//ok desafio piola

					$idDesafio = $consultaDesafioOk->fetch_array()["IDDESAFIO"];

					$fechaa = new DateTime(); 
					$fechaa->setTimeZone( new DateTimeZone('America/Buenos_Aires'));
					$fecha = $fechaa->format('Y-m-d');

					$query = "
					INSERT INTO publicaciones (IDDESAFIO, IDUSUARIO, FECHAHORA) VALUES ('$idDesafio', '$idusuario','$fecha')
					";
					if($con->query($query)) {
						echo 'ok';
					} else {
						echo 'error2';
					}
					mysqli_close($con);

				} else {
					echo "error2";
					mysqli_close($con);
				}


			} else {
				echo "error2";
				mysqli_close($con);
			}

			



		} else { echo 'error2'; mysqli_close($con); }
	} else { echo 'error2'; }
} else { echo 'error2'; }
?>
