<?php
if($_SERVER['REQUEST_METHOD']=="POST"){
	require_once("funciones.php");
	if(isset($_POST["token"]) && esSeguro($_POST["token"])) {
		require_once("conexion.php");
		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe
			$query = "
			SELECT desafios.IDDESAFIO as IDDESAFIO, desafios.DESAFIO as DESAFIO, usuarios.USUARIO as USUARIO, usuarios.TIENEIMAGEN as TIENEIMAGEN, desafios.IDUSUARIO as IDUSUARIO, desafios.FECHAHORA as FECHAHORA
			FROM (desafios
			INNER JOIN usuarios on desafios.IDUSUARIO = usuarios.IDUSUARIO)
			INNER JOIN seguimientos on usuarios.IDUSUARIO = seguimientos.IDUSUARIOSEGUIDO
			WHERE seguimientos.IDUSUARIOSEGUIDOR = '$idusuario'
			";
			$consulta = $con->query($query);
			$arrayDevolver = array();
			while($desafio = $consulta->fetch_array()) {
				$temp = array(
					"IDDESAFIO" => $desafio["IDDESAFIO"],
				    "IDUSUARIO" => $desafio["IDUSUARIO"],
				    "USUARIO" => $desafio["USUARIO"],
				    "TIENEIMAGEN" => $desafio["TIENEIMAGEN"],
				    "DESAFIO" => $desafio["DESAFIO"],
				    "FECHAHORA" => $desafio["FECHAHORA"]
				);
				$arrayDevolver[] = $temp;
			}
			echo json_encode($arrayDevolver, JSON_PRETTY_PRINT);
		} else { echo 'error2'; mysqli_close($con); }
	} else { echo 'error2'; }
} else { echo 'error2'; }
?>