<?php
// php가 mysql에 정보를 넣는곳이다.
  if(isset($_POST['id'])){
  include 'DB_connect/member_db_connect.php';
  $id = $_POST['id'];

  $sql = "DELETE FROM `mydog` WHERE id = '$id' ";
  $result = mysqli_query($conn, $sql);
  $row = mysqli_fetch_array($result);
}



?>
