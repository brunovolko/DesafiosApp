<?php 
	

	/*Code made by Bruno Volcovinsky | brunovolko@gmail.com*/
	/*Some words are written in Spanish*/

	/*
		
		Example of use:

		$dirTemp = $_FILES["image"]["tmp_name"]; // Image selected in a form's input. Only works with .jpg and .png images.
		$newImageName = "resizedImage"; // Name of rezised image.
		$destiny = "images/resized/"; // Destiny directory where the image will be saved.

		// You have to set the maximum width and height, so the image can be resized proportionally and without exceeding the chosen limits.
		$wishedWidth = 500;
		$wishedHeight = 425;
		$format = ".png"; // Wished format (Only .jpg or .png)
		if(redimensionar($dirTemp, $newImageName, $destiny, $wishedWidth, $wishedHeight, $format))
		{
			echo "ok";
		} else {
			echo "fail";
		}

	*/


	
	function redimensionar($dirTemp, $nuevoNombreImagen, $destino, $anchoDeseado, $altoDeseado, $formatoPreferencial) {


         
  		if (is_file($dirTemp)) {

        	list($anchoImagen, $altoImagen, $formatoImagen, $attr) = getimagesize($dirTemp);
 
				
			// Verificamos que sea jpg o png
			if ($formatoImagen == 2 || $formatoImagen == 3) {

				if($anchoImagen > $altoImagen) {
				
					$altoDeseado = $anchoDeseado * $altoImagen / $anchoImagen;
				
				} elseif($altoImagen > $anchoImagen) {
				
					$anchoDeseado = $altoDeseado * $anchoImagen / $altoImagen;
				
				} elseif($altoImagen == $anchoImagen) {

				}




				$destinoCompleto = $destino.$nuevoNombreImagen.$formatoPreferencial;
				
				switch ($formatoImagen) {
					case '2':

						// crear una imagen desde el original
					    $img = ImageCreateFromJPEG($dirTemp);
					    // crear una imagen nueva
					    $thumb = imagecreatetruecolor($anchoDeseado,$altoDeseado);
					    // redimensiona la imagen original copiandola en la imagen
					    ImageCopyResized($thumb,$img,0,0,0,0,$anchoDeseado,$altoDeseado,ImageSX($img),ImageSY($img));
					    // guardar la nueva imagen redimensionada
					    ImageJPEG($thumb,$destinoCompleto);
						break;

					case '3':
						// crear una imagen desde el original
					    $img = imagecreatefrompng($dirTemp);
					    // crear una imagen nueva
					    $thumb = imagecreatetruecolor($anchoDeseado,$altoDeseado);
					    // redimensiona la imagen original copiandola en la imagen
					    ImageCopyResized($thumb,$img,0,0,0,0,$anchoDeseado,$altoDeseado,ImageSX($img),ImageSY($img));
					    // guardar la nueva imagen redimensionada donde indicia $destino_temporal
					    imagepng($thumb,$destinoCompleto);
						break;
				}

				ImageDestroy($img);
				
				return true;


			} else { return false; }


					
						
		} else { return false; }



	}





 ?>