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
                while($desafio = $consulta->fetch_array()) {

                    $idPublicacion = $desafio["IDPUBLICACION"];

                    $consultaComentarios = $con->query("SELECT COUNT(IDCOMENTARIO) as CANTIDAD
                                        FROM comentarios
                                        WHERE IDPUBLICACION = '$idPublicacion' AND ESTADOCOMENTARIO = 'activo'
                                        ");
                    $cantComentarios = $consultaComentarios->fetch_array()["CANTIDAD"];

                    $temp = array(
                        "IDPUBLICACION" => $desafio["IDPUBLICACION"],
                        "DESAFIO" => $desafio["DESAFIO"],
                        "IDUSUARIO" => $desafio["IDUSUARIO"],
                        "USUARIO" => $desafio["USUARIO"],
                        "TIENEIMAGEN" => $desafio["TIENEIMAGEN"],
                        "CANTIDADCOMENTARIOS" => $cantComentarios
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