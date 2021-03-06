package com.rabbit.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @ClassName Task1
 * @Author pyt
 * @Date 2019/1/5 17:21
 */
public class Task1 {
    private final static String QUEUE_NAME = "task_queue";
    public static void main(String [] args) throws Exception{
        //1.创建服务器连接
        ConnectionFactory factory  = new ConnectionFactory();
        //指定连接的服务器主机地址
        factory.setHost("localhost");
        //2.创建连接，频道
        //Connection和Channel实现了java.io.closeable，所以可以不需要特意关闭
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //3.声明队列，队列声明方法queueDeclare具有幂等性。即只有当队列名不存在时才会被创建
        boolean durable = true ;//声明队列的持久化
        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
        String message = "Hello World";
        for (int i = 0 ; i < 10 ;i++){
            message += ".";
            //4.将消息发布到队列中
            //MessageProperties.PERSISTENT_TEXT_PLAIN——声明消息的持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("Send[x] Sent '" + message + "'");
        }
        message = "Hello World..........";
        for (int i = 10; i > 0 ;i--){
            message  = message.substring(0,message.length()-2);
            //4.将消息发布到队列中
            //MessageProperties.PERSISTENT_TEXT_PLAIN——声明消息的持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("Send[x] Sent '" + message + "'");
        }
    }
}
