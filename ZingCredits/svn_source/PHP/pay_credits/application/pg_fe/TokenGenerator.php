<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of TokenGenerator
 *
 * @author root
 */
$GLOBALS['THRIFT_ROOT'] = ROOT_PATH.'/library/Apache/Thrift';

require_once $GLOBALS['THRIFT_ROOT'].'/packages/zingme_payment_type/zingme_payment_type_types.php';
require_once $GLOBALS['THRIFT_ROOT'].'/packages/zingme_payment_service/TToken.php';
require_once $GLOBALS['THRIFT_ROOT'].'/transport/TSocket.php';
require_once $GLOBALS['THRIFT_ROOT'].'/protocol/TBinaryProtocol.php';
require_once $GLOBALS['THRIFT_ROOT'].'/transport/TFramedTransport.php';
require_once $GLOBALS['THRIFT_ROOT'].'/transport/TBufferedTransport.php';
class TokenGenerator {
    //put your code here
    private $client;
    private $transport;
    function  __construct() {
    }

    private function connect(){
        try{
	    $config = Zend_Registry::get('appconf');
 	    $options = array('host'=>$config->token->host,
                             'port'=>$config->token->port);
	
           /* $options = array('host'=>'10.199.18.36',
                             'port'=>7114);*/
            $socket = new TSocket($options['host'], $options['port']);
            $socket->setSendTimeout($config->token->connection->sendtimeout);
            $socket->setRecvTimeout($config->token->connection->sendtimeout);
            $this->transport = new TFramedTransport($socket);
            $protocol = new TBinaryProtocol($this->transport);
            $this->client = new TTokenClient($protocol);
        }catch(Exception $e){
            throw $e;
        }
    }

    public function getToken($userID){
        try{
            $this->connect();
            $this->transport->open();
            $token = $this->client->getToken($userID);
            $this->transport->close();
        }  catch (Exception $e){
            $this->transport->close();
            throw $e;
        }
        return $token;
    }
}
?>
