<?php
$_SERVER['REQUEST_METHOD']
if($_SERVER['REQUEST_METHOD']=="POST"){
	require_once("funciones.php");
	if(isset($_POST["token"]) && esSeguro($_POST["token"])) {
		require_once("conexion.php");
		$consulta = $con->query("SELECT tokens.IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST["token"]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$usuarioToken = $consulta->fetch_array()["IDUSUARIO"];
			if(isset($_POST["IDUSUARIO"]) && is_numeric($_POST["IDUSUARIO"])) {
				$idusuario = $_POST["IDUSUARIO"];
				if($idusuario == $usuarioToken) {
					// Esta logueado como un campeon, existe
					$query = "
					SELECT desafios.DESAFIO, usuarios.USUARIO, desafios.IDUSUARIO, desafios.FECHAHORA
					FROM (desafios
					INNER JOIN usuarios on desafios.IDUSUARIO = usuarios.IDUSUARIO)
					INNER JOIN seguimientos on usuarios.IDUSUARIO = seguimientos.IDUSUARIOSEGUIDO
					WHERE seguimientos.IDUSUARIOSEGUIDOR = '$idusuario'
					";
					$consulta = $con->query($query);
					$arrayDevolver = array();
					while($desafio = $consulta->fetch_array()) {
						$temp = array(
						    "IDUSUARIO" => $desafio["desafios.IDUSUARIO"],
						    "USUARIO" => $desafio["usuarios.USUARIO"],
						    "DESAFIO" => $desafio["desafios.DESAFIO"],
						    "FECHAHORA" => $desafio["desafios.FECHAHORA"]
						);
						$arrayDevolver[] = $temp;
					}
					echo json_encode($arrayDevolver, JSON_PRETTY_PRINT);
				}
			}
		}
		mysqli_close($con);
	}
}
?>