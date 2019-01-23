package com.rabbit.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName EmitLogDirect
 * @Description TODO
 * @Author pyt
 * @Date 2019/1/18 18:28
 * @Version
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String severity = getSeverity(argv);
        String message = getMessage(argv);
        /**
         * exchang_name，queue_name,
         */
        channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
        channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes("UTF-8"));
        channel.basicPublish(EXCHANGE_NAME, "warning", null, message.getBytes("UTF-8"));
        System.out.println("[x]Sent '" + severity + "‘：’" + message + "‘");
    }
    private static String getSeverity(String[] strings){
        if(strings.length < 1)
            return "error";
        return strings[0];
    }

    private static String getMessage(String[] strings){
        if(strings.length<2)
            return "info:Hello World！";
        return joinStrings(strings,"",1);
    }

    private static String joinStrings(String[] strings ,String delimiter,int startIndex){
        int length = strings.length;
        if(length == 0) return "";
        if(length < startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex+1;i<length;i++){
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
