<?php
    function get_body() {
        $txt;
        if ($_GET["type"] === "invite") {
            echo "invite";
        	$txt = "Hi!<br /><br />";
        	$txt .= $_GET["from"] . " invited you to participate in " . $_GET["event"];
        	$txt .= "<br /><br /><a href=\"http://mewap.se/mewap/app/index.html#/my-mewaps/". $_GET['id'] . "\">Click here to visit the meWap event!</a>";
        } else if ($_GET["type"] === "eachanswer") {
            echo "eachanswer";
            $txt = $_GET["from"] . " have answered your meWap " . $_GET["event"] . "<br /><br />";
        	$txt .= "<br /><br /><a href=\"http://mewap.se/mewap/app/index.html#/my-mewaps/". $_GET['id'] . "\">Click here to visit the meWap event!</a>";
        } else if ($_GET["type"] === "lastanswer") {
            echo "lastanswer";
            $txt = "Everyone have answered your meWap " . $_GET["event"] . "<br /><br />";
        	$txt .= "<br /><br /><a href=\"http://mewap.se/mewap/app/index.html#/my-mewaps/". $_GET['id'] . "\">Click here to visit the meWap event!</a>";
        }
        return $txt;
    }
    
    function get_subject() {
        $subject;
        if ($_GET["type"] === "invite") {
	        $subject = $_GET["event"] . " - " . $_GET["from"] . " invited you to participate in an event using meWap";
        } else if ($_GET["type"] === "eachanswer") {
	        $subject = $_GET["event"] . " - " . $_GET["from"] . " have answered your meWap";
        } else if ($_GET["type"] === "lastanswer") {
	        $subject = $_GET["event"] . " - Everyone have answered your meWap";
        }
        return $subject;
    }

	$to = $_GET["to"];
	$subject = get_subject();
	$txt = get_body();
	$headers = "From: mewap@oskarnyberg.com" . "\r\n";
    $headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";

	echo mail($to,$subject,$txt,$headers);
?>
