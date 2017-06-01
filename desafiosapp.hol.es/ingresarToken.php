<?php

require_once("funciones.php");
if(isset($_POST['token']) && !empty($_POST['token']) && esSeguro($_POST['token'])) {
	require_once("conexion.php");
	$consulta = $con->query("SELECT usuarios.IDUSUARIO as IDUSUARIO, usuarios.USUARIO as USUARIO FROM usuarios INNER JOIN tokens ON usuarios.IDUSUARIO = tokens.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
	if($consulta->num_rows == 1) {
		$registro = $consulta->fetch_array();
			

		/*//El usuario pudo haber cerrado sesion sin estar conectado a internet, por lo que la tabla puede estar desactualizada, asi que borramos.
		$con->query("DELETE FROM tokens WHERE IDUSUARIO='$registro[IDUSUARIO]' LIMIT 1");

		// Guardar token
		$con->query("INSERT INTO tokens (TOKEN, IDUSUARIO) VALUES ('$hash', '$registro[IDUSUARIO]')");*/

		$json = array("Token" => $_POST["token"], "IDUsuario" => $registro["IDUSUARIO"], "Usuario" => $registro["USUARIO"]);
		echo json_encode($json);
		mysqli_close($con);
		



	} else { echo "error2"; mysqli_close($con);}



} else { echo "error2"; }

?>