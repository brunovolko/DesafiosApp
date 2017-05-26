<?php 

function AsegurarToken(){
	$consulta = $con->query("SELECT tokens.IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST["token"]' AND usuarios.ESTADO='activo' LIMIT 1");
		if($consulta->num_rows == 1) {// encontró el token
		$usuarioToken = $consulta->fetch_array()["IDUSUARIO"];
		if(isset($_POST["IDUSUARIO"]) && is_numeric($_POST["IDUSUARIO"])) {
				$idusuario = $_POST["IDUSUARIO"];
				if($idusuario == $usuarioToken) {

				}
				else{
				die();	
					}
}