<?php

if($_SERVER['REQUEST_METHOD']=="GET"){
	
	if(isset($_GET['usuario']) && !empty($_GET['usuario']) && isset($_GET['password']) && !empty($_GET['password'])){
		require_once("funciones.php");
		require_once("conexion.php");
		$usuario = asegurar($_GET['usuario']);
		// Hashear contraseña 
			$consulta=$con->query("SELECT IDUSUARIO FROM usuarios WHERE usuario='$usuario' AND password='$password' LIMIT 1")

			/*$consulta = $con->query("SELECT tokens.IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST["token"]' AND usuarios.ESTADO='activo' LIMIT 1");
			if($consulta->num_rows == 1) {
					// encontró el token
					$usuarioToken = $consulta->fetch_array()["IDUSUARIO"];

			if(isset($_GET["IDUSUARIO"]) && is_numeric($_GET["IDUSUARIO"])) {
				$idusuario = $_GET["IDUSUARIO"];*/
				if($idusuario == $usuarioToken) {
					require_once("listardesafios.php");
					echo "el Usuario se ingreso correctamente";

				}
			}
		}
	}
}
} else {
	echo "error";
}









?>