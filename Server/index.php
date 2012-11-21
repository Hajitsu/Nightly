<?php

if (isset($_POST['tag']) && $_POST['tag'] != '') {

    $tag = $_POST['tag'];

    require_once ("db_functions.php");

    $db = new DB_Functions();

    $response = array();

    if ($tag == 'login') {

        $user_email = $_POST['user_email'];
        $user_password = $_POST['user_password'];

        $user = $db->login($user_email, $user_password);
        if ($user) {

            $response["success"] = "1";
            $response["user_id"] = $user["id"];
            $response["user_email"] = $user["user_email"];
            $response["user_password"] = $user["user_password"];
            $response["user_name"] = $user["user_name"];

            echo json_encode($response);

            error_log("Index Login Json >>" . json_encode($response) . "\r\n", 3, "Log.log");

        } else {

            $response["error"] = "1";
            $response["error_message"] = "ایمیل یا گذرواژه نادرست است.";

            echo json_encode($response);
        }

    } else
        if ($tag == 'register') {

            $user_email = $_POST['user_email'];
            $user_password = $_POST['user_password'];
            $user_name = $_POST['user_name'];

            if ($db->isUserExisted($user_email)) {

                $response["error"] = "2";
                $response["error_message"] = "این ایمیل قبلا ثبت شده است";

                echo json_encode($response, JSON_FORCE_OBJECT);

            } else {

                $user = $db->register($user_email, $user_password, $user_name, $user_sex);
                if ($user) {

                    // User registered.
                    $response["success"] = "1";
                    $response["user_id"] = $user["id"];
                    $response["user_email"] = $user["user_email"];
                    $response["user_password"] = $user["user_password"];
                    $response["user_name"] = $user["user_name"];

                    error_log("Index Register Json >>" . json_encode($response) . "\r\n", 3,
                        "Log.log");

                    $headers = "From: shabane@1mohammadi.ir\r\n";
                    $headers .= "Reply-To: " . $user_email . "\r\n";
                    $headers .= "MIME-Version: 1.0\r\n";
                    $headers .= "Content-Type: text/html; charset=UTF-8\r\n";

                    $message = '<html><body><div style="float:right"><p>';
                    $message .= $user_name;
                    $message .= "</p><p>ایمیل شما در شبانه فعال شد.</p><p><label>پست الکترونیکی : </label>";
                    $message .= $user_email;
                    $message .= "</p><p><label>گذرواژه : </label>";
                    $message .= $user_password;
                    $message .= "</p></div></body></html>";

                    mail($user_email, "عضویت در شبانه", $message, $headers);

                    echo json_encode($response, JSON_FORCE_OBJECT);
                } else {

                    // User failed to register.
                    $response["error"] = "1";
                    $response["error_message"] = "هنگام ثبت نام مشکلی پیش آمد.دوباره تلاش کنید.";
                    echo json_encode($response, JSON_FORCE_OBJECT);
                }
            }

        } else
            if ($tag == 'savenote') {

                $user_id = $_POST['user_id'];
                $note_content = $_POST['note_content'];
                $result = $db->saveNote($user_id, $note_content);

                if ($result) {
                    error_log("Index storeNote Json >>" . json_encode($result) . "\r\n", 3,
                        "Log.log");
                    $response["success"] = "1";
                    echo json_encode($response, JSON_FORCE_OBJECT);
                } else {
                    $response["error"] = "1";
                    $response["error_message"] = "شبانه ذخیره نشد :-(";
                    echo json_encode($response, JSON_FORCE_OBJECT);
                }


            } else
                if ($tag == 'updateNotes') {
                    $note_id = $_POST['note_id'];
                    $note_content = $_POST['note_content'];

                    error_log("Index updateNotes params >>" . $note_content . "\r\n", 3, "Log.log");
                    $result = $db->updateNotes($note_id, $note_content);

                    if ($result) {
                        error_log("Index updateNotes Json >>" . json_encode($result) . "\r\n", 3,
                            "Log.log");
                        $response["success"] = "1";
                        echo json_encode($response, JSON_FORCE_OBJECT);
                    } else {
                        $response["error"] = "1";
                        $response["error_message"] = "شبانه ویرایش نشد :-(";
                        echo json_encode($response, JSON_FORCE_OBJECT);
                    }

                } else
                    if ($tag == 'deleteNotes') {

                        $note_id = $_POST['note_id'];
                        $result = $db->deleteNotes($note_id);

                        if ($result) {
                            error_log("Index deleteNotes Json >>" . json_encode($result) . "\r\n", 3,
                                "Log.log");
                            $response["success"] = "1";
                            echo json_encode($response, JSON_FORCE_OBJECT);
                        } else {
                            $response["error"] = "1";
                            $response["error_message"] = "شبانه پاک نشد :-(";
                            echo json_encode($response, JSON_FORCE_OBJECT);
                        }

                    } else
                        if ($tag == 'getNotesList') {

                            $user_id = $_POST['user_id'];
                            $result = $db->getNotesList($user_id);

                            if ($result) {
                                error_log("Index getNotesList Json >>" . json_encode($result) . "\r\n", 3,
                                    "Log.log");
                                echo json_encode($result, JSON_FORCE_OBJECT);
                            } else {
                                $response["error"] = "1";
                                $response["error_message"] = "شما هنوز شبانه ای ننوشته اید.";
                                echo json_encode($response, JSON_FORCE_OBJECT);
                            }
                        } else
                            if ($tag == 'getNoteDetail') {

                                $note_id = $_POST['note_id'];

                                $result = $db->getNoteDetail($note_id);
                                if ($result) {

                                    $response['success'] = "1";
                                    $response['note_content'] = $result['note_content'];
                                    echo json_encode($response, JSON_FORCE_OBJECT);

                                } else {

                                    $response["error"] = "1";
                                    $response["error_message"] = "شبانه پیدا نشد :-(";
                                    echo json_encode($response, JSON_FORCE_OBJECT);

                                }
                            }
}
?>