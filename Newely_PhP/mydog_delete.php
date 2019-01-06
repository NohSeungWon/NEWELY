<?php

  if(isset($_POST['id'])){
  include 'DB_connect/member_db_connect.php';
  $desertionno = $_POST['desertionno'];
  $id = $_POST['id'];

  $sql = " select * from mydog where id ='$id' and desertionno = '$desertionno' ";
  $result = mysqli_query($conn, $sql);
  $row = mysqli_fetch_array($result);

  if ($id == $row['id'] && $desertionno == $row['desertionno']) {

    $query = "DELETE FROM `mydog` WHERE desertionno = '$desertionno'  and id = '$id' ";
    $result = mysqli_query($conn, $query);

  }
}

?>
