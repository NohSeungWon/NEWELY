<?php
// php가 mysql에 정보를 넣는곳이다.
  if(isset($_POST['id'])){
  include 'DB_connect/member_db_connect.php';
  $desertionno = $_POST['desertionno'];
  $specialMark = $_POST['specialMark'];
  $age = $_POST['age'];
  $careaddr = $_POST['careaddr'];
  $sexcd = $_POST['sexcd'];
  $caretel = $_POST['caretel'];
  $kindcd = $_POST['kindcd'];
  $carenm = $_POST['carenm'];
  $id = $_POST['id'];
  $orgnm = $_POST['orgnm'];
  $popfile = $_POST['popfile'];

  $sql = " select * from mydog where desertionno ='$desertionno'";
  $result = mysqli_query($conn, $sql);
  $row = mysqli_fetch_array($result);

  if ($desertionno != $row['desertionno']) {
    echo "로우".$row['desertionno'];
    echo "디설 :".$desertionno;

    $query = "INSERT INTO mydog(desertionno, specialMark, age,
    careaddr, sexcd, caretel, kindcd, carenm, id, orgnm, popfile)
     VALUES ('$desertionno','$specialMark','$age','$careaddr',
              '$sexcd','$caretel','$kindcd','$carenm','$id','$orgnm','$popfile')";

    mysqli_set_charset($conn,"utf8"); //한글 깨짐현상 해결
    $result= mysqli_query($conn,$query);
  }


}



?>
