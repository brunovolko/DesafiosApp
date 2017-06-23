<?php
if($_SERVER['REQUEST_METHOD']=="POST"){
	require_once("funciones.php");
	if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['busqueda']) && !empty($_POST['busqueda'])) {
		require_once("conexion.php");
		$consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {
			// encontró el token
			$idusuario = $consulta->fetch_array()["IDUSUARIO"];
			// Esta logueado como un campeon, existe
			$usuarioIngresado = strtolower(asegurar(trim($_POST['busqueda'])));
			$query = "
			SELECT USUARIO, IDUSUARIO, TIENEIMAGEN
			FROM usuarios
			WHERE USUARIO LIKE '%$usuarioIngresado%' AND ESTADO='activo'
			";
			$consulta = $con->query($query);
			$arrayDevolver = array();
			while($usuario = $consulta->fetch_array()) {
				$temp = array(
				    "IDUSUARIO" => $usuario["IDUSUARIO"],
				    "USUARIO" => $usuario["USUARIO"],
				    "TIENEIMAGEN" => $usuario["TIENEIMAGEN"]
				);
				$arrayDevolver[] = $temp;
			}
			echo json_encode($arrayDevolver, JSON_PRETTY_PRINT);
			mysqli_close($con);
		} else { echo 'error2'; mysqli_close($con); }
	} else { echo 'error2'; }
} else { echo 'error2'; }
?>