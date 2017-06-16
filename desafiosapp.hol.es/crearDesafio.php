<?php
if($_SERVER['REQUEST_METHOD']=="POST"){
	require_once("funciones.php");
	if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['desafio']) && !empty(trim($_POST['desafio'])) && strlen($_POST['desafio']) <= 140) {
		require_once("conexion.php");
		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe
			$desafio = asegurar(trim($_POST['desafio']));
			$fechaa = new DateTime(); 
			$fechaa->setTimeZone( new DateTimeZone('America/Buenos_Aires'));
			$fecha = $fechaa->format('Y-m-d');
			$query = "
				INSERT INTO desafios (DESAFIO, FECHAHORA, IDUSUARIO) VALUES ('$desafio', '$fecha','$idusuario')
			";
			if($con->query($query)) {
				echo 'ok';
			} else {
				echo 'error2';
			}
			mysqli_close($con);
		} else { echo 'error2'; mysqli_close($con); }
	} else { echo 'error2'; }
} else { echo 'error2'; }
?>