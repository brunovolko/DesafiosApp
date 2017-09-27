<?php
if($_SERVER['REQUEST_METHOD']=="POST") {
    require_once("funciones.php");

    if(isset($_POST["token"]) && esSeguro($_POST["token"]) && isset($_POST['idPublicacion']) && is_numeric($_POST['idPublicacion'])) {
        require_once("conexion.php");

        $consulta = $con->query("SELECT tokens.IDUSUARIO as IDUSUARIO FROM tokens INNER JOIN usuarios on tokens.IDUSUARIO = usuarios.IDUSUARIO WHERE tokens.TOKEN='$_POST[token]' AND usuarios.ESTADO='activo' LIMIT 1");
        if($consulta->num_rows == 1) {
            // encontró el token
            $idusuario = $consulta->fetch_array()["IDUSUARIO"];
            // Esta logueado como un campeon, existe

            //Ahora a chekear la publicacion a leer
            $consulta = $con->query("SELECT IDPUBLICACION FROM publicaciones WHERE IDPUBLICACION='$_POST[idPublicacion]' AND ESTADO='activa' LIMIT 1");

            if($consulta->num_rows == 1) {
                //Okk

                $idPublicacionALeer = $_POST['idPublicacion'];

                $consulta = $con->query("SELECT comentarios.IDCOMENTARIO as IDCOMENTARIO, comentarios.COMENTARIO as COMENTARIO, usuarios.IDUSUARIO as IDUSUARIO, usuarios.USUARIO as USUARIO
                                        FROM comentarios
                                        INNER JOIN usuarios on comentarios.IDUSUARIO = usuarios.IDUSUARIO
                                        WHERE comentarios.IDPUBLICACION = '$idPublicacionALeer' AND comentarios.ESTADOCOMENTARIO = 'activo' AND usuarios.ESTADO = 'activo'
                                        ORDER BY publicaciones.IDPUBLICACION DESC");

                $arrayDevolver = array();
                while($comentario = $consulta->fetch_array()) {

                    $temp = array(
                        "IDCOMENTARIO" => (int)$comentario["IDCOMENTARIO"],
                        "COMENTARIO" => $comentario["COMENTARIO"],
                        "IDUSUARIO" => (int)$comentario["IDUSUARIO"],
                        "USUARIO" => $comentario["USUARIO"],
                        "IDPUBLICACION" => (int)$idPublicacionALeer
                    );
                    $arrayDevolver[] = $temp;
                }
                echo json_encode($arrayDevolver, JSON_PRETTY_PRINT);

                
                mysqli_close($con);

            } else { echo 'error2'; mysqli_close($con); }


            

        } else { echo 'error2'; mysqli_close($con); }
        
    } else { echo 'error2'; }

} else { echo 'error2'; }

?>