<?php

$sid = $_POST["sid"];

$dbname = dirname($_SERVER["SCRIPT_FILENAME"]) . "/airport";
$dbh = new PDO("sqlite:$dbname");
$dbh->beginTransaction();

$stmnt = $dbh->prepare('select rname from restaurants where sid="'.$sid.'"');
$stmnt->execute() or die(print_r($dbh->errorInfo(), true));

while ($row = $stmnt->fetch()) {
	$output .= $row['rname'].",";
}

print($output);
?>