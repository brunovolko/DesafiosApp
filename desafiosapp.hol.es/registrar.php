<?php 

	require_once("../../funciones.php");
	session_start();
	if(!isset($_SESSION) || empty($_SESSION['IDUSUARIO'])) {
		if(isset($_GET['email']) && !empty($_GET['email']) && esEmail($_GET['email'])) {
				$email = trim(strtolower(asegurar($_GET['email'])));
				if(isset($_POST['password']) && strlen($_GET['password']) > 5) {
					require_once("../../conexion.php");
					$ExisteEmail = $con->query("SELECT ESTADO FROM USUARIOS WHERE EMAIL='$email' LIMIT 1");
					if($ExisteEmail->num_rows == 1) {
						$estado = $ExisteEmail->fetch_array()["ESTADO"];
                           }
                           if ()
						$con->query("INSERT INTO USUARIOS (EMAIL,CONTRASENA,HASH) VALUES ('$email','$contrasena','$hash')");