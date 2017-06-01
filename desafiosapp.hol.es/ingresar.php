<?php

if(isset($_POST['usuario']) && !empty($_POST['usuario']) && isset($_POST['password']) && !empty($_POST['password'])) {
	require_once("funciones.php");
	$usuario = asegurar($_POST['usuario']);
	require_once("conexion.php");
	$consulta = $con->query("SELECT IDUSUARIO, USUARIO, CONTRASENA FROM usuarios WHERE USUARIO='$usuario' AND ESTADO='activo' LIMIT 1");
	if($consulta->num_rows == 1) {
		require_once("PasswordHash.php");
		$hasher = new PasswordHash(8, FALSE);
		$registro = $consulta->fetch_array();
		$contrasena = $registro["CONTRASENA"];
		if ($hasher->CheckPassword($_POST['password'], $contrasena)) {
			//Generar token
			$caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"; //posibles caracteres a usar
			$numerodeletras=22; //numero de letras para generar el texto
			$hash = ""; //variable para almacenar la cadena generada
			for($i=0;$i<$numerodeletras;$i++)
			{
			    $hash .= substr($caracteres,rand(0,strlen($caracteres)),1); //Extraemos 1 caracter de los caracteres entre el rango 0 a Numero de letras que tiene la cadena
			}

			//El usuario pudo haber cerrado sesion sin estar conectado a internet, por lo que la tabla puede estar desactualizada, asi que borramos.
			$con->query("DELETE FROM tokens WHERE IDUSUARIO='$registro[IDUSUARIO]' LIMIT 1");

			// Guardar token
			$con->query("INSERT INTO tokens (TOKEN, IDUSUARIO) VALUES ('$hash', '$registro[IDUSUARIO]')");

			$json = array("Token" => $hash, "IDUsuario" => $registro["IDUSUARIO"], "Usuario" => $registro["USUARIO"]);
			echo json_encode($json);
			mysqli_close($con);
		} else { echo "error2"; mysqli_close($con);}
		



	} else { echo "error2"; mysqli_close($con);}



} else { echo "error2"; }

?>