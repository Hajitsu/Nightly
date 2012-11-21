a<?php
class DB_CONNECT
{
    function __construct()
    {
        $this->connect();
    }

    function __destruct()
    {
        $this->close();
    }

    function connect()
    {
        require_once __dir__ . '/db_config.php';

        $con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());
        mysql_query('set names utf8') or die(mysql_error());
        mysql_set_charset("utf8", $con);
        //mysql_query("SET CHARACTER SET 'utf8'", $con);

        $db = mysql_select_db(DB_DATABASE) or die(mysql_error());

        return $con;
    }

    function close()
    {
        mysql_close();
    }
}
?>