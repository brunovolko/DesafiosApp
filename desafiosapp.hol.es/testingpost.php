<?php 
	if(isset($_POST['1'])) {
		if(!empty($_POST['1'])) {
			if(isset($_POST['2'])) {
				if(!empty($_POST['2'])) {
					echo "5";
				} else {echo "4";}
			} else {echo "3";}
		} else {echo "2";}
	} else {echo "1";}
?>