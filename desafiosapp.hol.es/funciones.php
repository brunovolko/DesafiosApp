<?php 

	function diaDeHoy() {
		$date = new DateTime(); 
		$date->setTimeZone( new DateTimeZone('America/Buenos_Aires'));
		return $date->format('d/m/Y');
	}
	function getAge($fechaIngresada)
	{
		$actual = explode("/", diaDeHoy());
		$fechaNacimiento = explode("-", $fechaIngresada);
		if($fechaNacimiento[1] < $actual[1]) {
			return $actual[2] - $fechaNacimiento[0];
		} elseif($fechaNacimiento[1] > $actual[1]) {
			return (($actual[2] - $fechaNacimiento[0])-1);
		} elseif($fechaNacimiento[2] <= $actual[0]) {
			return $actual[2] - $fechaNacimiento[0];
		} else {
			return (($actual[2] - $fechaNacimiento[0])-1);
		}
	}
	if(!function_exists('fechaValida')) {
		function fechaValida($fecha) {
			if (false === strtotime($fecha)) 
		    { 
		        return false;
		    } 
		    else
		    { 
		        list($year, $month, $day) = explode('-', $fecha); 
		        if (false === checkdate($month, $day, $year)) 
		        { 
		            return false;
		        } 
		    } 
		    return true;
		}
	}
	if(!function_exists('getIp')) {
		function getIp() {
			if(!empty($_SERVER['HTTP_CLIENT_IP'])) {
			    $ip=$_SERVER['HTTP_CLIENT_IP'];
			} elseif(!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
			    $ip=$_SERVER['HTTP_X_FORWARDED_FOR'];
			} else {
			    $ip=$_SERVER['REMOTE_ADDR'];
			}
			return $ip;
		}
	}
	if (!function_exists('esSeguro')) {
			function esSeguro($texto) {
				$busqueda1 = stripos($texto, '"');
				$busqueda2 = stripos($texto, "<");
				$busqueda3 = stripos($texto, ">");
				$busqueda4 = stripos($texto, "'");
				$busqueda5 = stripos($texto, "{");
				$busqueda6 = stripos($texto, "}");
				$busqueda7 = stripos($texto, "(");
				$busqueda8 = stripos($texto, ")");
				$busqueda9 = stripos($texto, ";");
				$busqueda10 = stripos($texto, "/");
				$busqueda11 = stripos($texto, "-");
				if($busqueda1 !== FALSE  || $busqueda2 !== FALSE || $busqueda3 !== FALSE  ||  $busqueda4 !== FALSE  ||  $busqueda5 !== FALSE  || $busqueda6 !== FALSE || $busqueda7 !== FALSE  || $busqueda8 !== FALSE || $busqueda9 !== FALSE || $busqueda10 !== FALSE || $busqueda11 !== FALSE){
					return false;
				} else {
					return true;
				}
			}
	}
	if (!function_exists('esEmail')) {
		function esEmail($email) {
			$mail_correcto = false; 
		   	//compruebo unas cosas primeras 
		   	if ((strlen($email) >= 6) && (substr_count($email,"@") == 1) && (substr($email,0,1) != "@") && (substr($email,strlen($email)-1,1) != "@")){ 
		      	if ((!strstr($email,"'")) && (!strstr($email,"\"")) && (!strstr($email,"\\")) && (!strstr($email,"\$")) && (!strstr($email," "))) { 
		         	//miro si tiene caracter . 
		         	if (substr_count($email,".")>= 1){ 
		            	//obtengo la terminacion del dominio 
		            	$term_dom = substr(strrchr ($email, '.'),1); 
		            	//compruebo que la terminación del dominio sea correcta 
		            	if (strlen($term_dom)>1 && strlen($term_dom)<5 && (!strstr($term_dom,"@")) ){ 
		               	//compruebo que lo de antes del dominio sea correcto 
		               	$antes_dom = substr($email,0,strlen($email) - strlen($term_dom) - 1); 
		               	$caracter_ult = substr($antes_dom,strlen($antes_dom)-1,1); 
			               	if ($caracter_ult != "@" && $caracter_ult != "."){ 
			                  	$mail_correcto = true; 
			               	} 
		            	} 
		         	} 
		      	} 
		   	} 
		   	if($mail_correcto == true) {
		   		return true;
		   	} else {
		   		return false;
		   	}
		}
	}
	
	
	
	if (!function_exists('asegurar')) {
		function asegurar($temp) {
			$temp = str_replace("\"", "&quot;", $temp);
			$temp = str_replace("'", "&apos;", $temp);
			$temp = str_replace("(", "&#40;", $temp);
			$temp = str_replace(")", "&#41;", $temp);
			$temp = str_replace("-", "&#45;", $temp);
			$temp = str_replace("{", "&#123;", $temp);
			$temp = str_replace("}", "&#125;", $temp);
			$temp = str_replace("<", "&lt;", $temp);
			$temp = str_replace(">", "&gt;", $temp);
			$temp = str_replace("/", "&#47;", $temp);
			return $temp;
		}
	}

	function desAsegurar($temp) {
		$temp = str_replace("&quot;", "\"", $temp);
		$temp = str_replace("&apos;", "'", $temp);
		$temp = str_replace("&#40;", "(", $temp);
		$temp = str_replace("&#41;", ")", $temp);
		$temp = str_replace("&#45;", "-", $temp);
		$temp = str_replace("&#123;", "{", $temp);
		$temp = str_replace("&#125;", "}", $temp);
		$temp = str_replace("&lt;", "<", $temp);
		$temp = str_replace("&gt;", ">", $temp);
		$temp = str_replace("&#47;", "/", $temp);
		return $temp;
	}
 ?>