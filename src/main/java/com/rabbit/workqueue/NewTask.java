package com.rabbit.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName NewTask
 * @Author pyt
 * @Date 2019/1/5 14:35
 */
public class NewTask {
    private final static String QUEUE_NAME = "hello";
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
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "Hello World";
        for (int i = 0 ; i < 10 ;i++){
            message += ".";
            //4.将消息发布到队列中
            channel.basicPublish("", QUEUE_NAME,null,message.getBytes());
            System.out.println("Send[x] Sent '" + message + "'");
        }
    }
}
