
<?php
if (isset($_POST['id'])) {
  include 'member_db_connect.php';
  $id = $_POST['id'];
  // $id= "pro";
  // $password="q2w";
  // $query = " select * from user where id ='$id'";

  $query = " select * from mydog where id ='$id'";
  // mysqli_set_charset($conn,"utf8"); //한글 깨짐현상 해결
  $result = $conn->query($query);
  $total_record = $result->num_rows;

  $result_array = array();

  for ( $i = 0; $i < $total_record; $i++ ) {
    // 한행씩 읽기 위해 offset을 준다.
    $result->data_seek($i);
    // $row['nickname'] = iconv("EUC-KR","UTF-8", $row['nickname']);
    // 결과값을 배열로 바꾼다.
    $row = $result->fetch_array();
    // 결과값들을 JSON형식으로 넣기 위해 연관배열로 넣는다.
    $row_array = array(
      "desertionno" => $row['desertionno'],
      "specialMark" => $row['specialMark'],
      "age" => $row['age'],
      "careaddr" => $row['careaddr'],
      "sexcd" => $row['sexcd'],
      "caretel" => $row['caretel'],
      "kindcd" => $row['kindcd'],
      "carenm" => $row['carenm'],
      "orgnm" => $row['orgnm'],
      "popfile" => $row['popfile']
      );
    // 한 행을 results에 넣을 배열의 끝에 추가한다.
    array_push($result_array,$row_array);
  }

  // 위에서 얻은 결과를 다시 JSON형식으로 넣는다.
  $arr = array(
    // "status" => "OK",
    // "num_result" => "$total_record",
    "results" => $result_array
    );

  // 만든건 그냥 배열이므로 JSON형식으로 인코딩을 한다.
  //JSON_UNESCAPED_UNICODE 이거 안해주면 오류난다. 한글 깨짐
  $json_array = json_encode($arr, JSON_UNESCAPED_UNICODE);
  // 인코딩한 JSON배열을 출력한다.
  print_r($json_array);
}
  //
  // $result = mysqli_query($conn, $query);
  // $row = mysqli_fetch_array($result);
  // echo $row['nickname'];
// // if ($id==$row['id'] && $password==$row['password'])
//   if ($row['distinguish']==1) {
//     echo 1;
//   }else {
//     echo 2;
//   }
// }

?>
