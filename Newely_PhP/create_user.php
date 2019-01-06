<?php
// php가 mysql에 정보를 넣는곳이다.
  if(isset($_GET['id'])){
  include 'DB_connect/member_db_connect.php';
  $id = $_GET['id'];
  $password = $_GET['password'];
  $nickname = $_GET['nickname'];
  $distinguish = $_GET['distinguish'];
  if (!isset($_GET['adress'])) {
    $adress = "none";
  }else {
    $adress = $_GET['adress'];
  }

// 아이디 중복체크
// $sql = " select * from user where id ='{$_GET['id']}' and password='{$_GET['password']}'";
$sql = " select * from userinfo where id ='{$_GET['id']}'";
$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_array($result);
//보낸 정보와 디비가 겹칠 때


if ($id==$row['id']) { //겹칠 때
  echo 2;
  // echo $distinguish;
  }else { // 겹치지 않을 때
  echo 1;
  // echo $distinguish;
  $query = "INSERT INTO userinfo(id, password, nickname, adress, distinguish)
            VALUES ('$id','$password','$nickname','$adress','$distinguish')";
            // VALUES ('t@t.','tt1','한글','dd')";
  mysqli_set_charset($conn,"utf8"); //한글 깨짐현상 해결
  $result= mysqli_query($conn,$query);
}
}

// $query = "INSERT INTO user(id, password, adress, nickname)
//  VALUES ('orr@da.com','test123','리얼리','굉장히')";

// $query = "INSERT INTO member (id, password, adress, nickname)
// VALUES ('dd','ddd','none','ddddd')";
// $query = "INSERT into member(id,password,adress,nickname)
// values('$id', '$password', '$adress','$nickname')";
// $result= mysqli_query($conn,$query);
// }

?>
