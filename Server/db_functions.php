<?php

class DB_Functions
{

    private $db;

    function __construct()
    {
        require_once ("db_connect.php");

        $this->db = new DB_CONNECT();
        $this->db->connect();
    }

    function __destruct()
    {

    }

    public function register($user_email, $user_password, $user_name)
    {
        include_once ("jdf.php");
        $time_now = jdate('Y/n/j');

        $user_email = mysql_real_escape_string($user_email);
        $user_password = mysql_real_escape_string($user_password);
        $user_name = mysql_real_escape_string($user_name);

        $user_password = md5($user_password);

        $result = mysql_query("INSERT INTO `tbl_users`(`user_email`, `user_password`, `user_name`, `user_registration_time`) VALUES ('$user_email', '$user_password', '$user_name', '$date_now')") or
            die(mysql_error());
        if ($result) {
            $uid = mysql_insert_id();

            error_log("Register >> uid : " . $uid . "\r\n", 3, "Log.log");

            $result = mysql_query("SELECT * FROM `tbl_users` WHERE `id`='$uid'") or die(mysql_error
                ());
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    public function login($user_email, $user_password)
    {
        $user_email = mysql_real_escape_string($user_email);
        $user_password = mysql_real_escape_string($user_password);

        $user_password = md5($user_password);

        $result = mysql_query("SELECT * FROM `tbl_users` WHERE `user_email` = '$user_email' AND `user_password` = '$user_password'") or
            die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    public function isUserExisted($user_email)
    {
        $user_email = mysql_real_escape_string($user_email);

        $result = mysql_query("SELECT `id` FROM `tbl_users` WHERE `user_email` = '$user_email'") or
            die(mysql_error());

        error_log("isUserExisted >> Email : " . $user_email . ", Result : " . $result .
            "\r\n", 3, "Log.log");

        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed.
            return true;
        } else {
            // user not existed.
            return false;
        }
    }

    public function saveNote($user_id, $note_content)
    {
        include_once ("jdf.php");
        $date_now = jdate('Y/n/j');

        $note_content = $note_content = mysql_real_escape_string($note_content);

        $note_subject = $this->createSubject($note_content, 0, 15);
        $note_subject = mysql_real_escape_string($note_subject);

        $result = mysql_query("INSERT INTO `tbl_notes`(`user_id`, `note_subject`, `note_date`,`note_content`) VALUES ('$user_id', '$note_subject', '$date_now','$note_content')") or
            die(mysql_error());

        error_log("storeNote >> result : " . $result . " >> at : " . $date_now . "\r\n",
            3, "Log.log");

        return $result;
    }

    public function updateNotes($note_id, $note_content)
    {
        include_once ("jdf.php");
        $note_date = jdate('Y/n/j');

        $note_content = mysql_real_escape_string($note_content);

        $note_subject = $this->createSubject($note_content, 0, 15);
        $note_subject = mysql_real_escape_string($note_subject);

        $result = mysql_query("UPDATE `tbl_notes` SET `note_subject`='$note_subject', `note_date`='$note_date', `note_content`='$note_content' WHERE `id` = '$note_id'") or
            die(mysql_error());

        error_log("updateNotes >> result : " . $result . " >> at : " . $date_now . "\r\n",
            3, "Log.log");

        return ($result);
    }

    public function deleteNotes($note_id)
    {
        $result = mysql_query("UPDATE `tbl_notes` SET `note_deleted`=1 WHERE `id` = '$note_id' ") or
            die(mysql_error());

        error_log("deleteNotes >> result : " . $result . "\r\n", 3, "Log.log");

        return ($result);
    }

    public function getNoteDetail($note_id)
    {
        $result = mysql_query("SELECT `note_content` FROM `tbl_notes` WHERE `id` = '$note_id'") or
            die(mysql_error());
        if (mysql_num_rows($result) > 0) {
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    public function getNotesList($user_id)
    {
        $result = mysql_query("SELECT `id`, `note_subject`, `note_date` FROM `tbl_notes` WHERE `user_id` = '$user_id' AND `note_deleted`=0 ") or
            die(mysql_error());

        if (mysql_num_rows($result) > 0) {

            //$response = array();

            $response["notes"] = array();

            while ($row = mysql_fetch_array($result)) {
                $note = array();
                $note["note_id"] = $row["id"];
                $note["note_subject"] = $row["note_subject"];
                $note["note_date"] = $row["note_date"];

                array_push($response["notes"], $note);
            }
            $response["success"] = "1";

            return $response;
        }
    }

    private function createSubject($string, $startIndex, $endIndex)
    {
        return substr($string, $startIndex, $endIndex);
    }
}

?>