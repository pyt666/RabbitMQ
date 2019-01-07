package com.rabbit.publish_subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @ClassName Task1
 * @Author pyt
 * @Date 2019/1/5 17:21
 */
public class Producer {
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
        //3.声明交易所,exchange名为logs，类型为fanout
        channel.exchangeDeclare("logs","fanout");
        //声明队列方法未传入参数时，
        // 该队列为non-durable, exclusive, autodelete（非持久化，专一，自动删除的）
        String queueName = channel.queueDeclare().getQueue();
        //4.交易所绑定队列
        channel.queueBind(queueName,"logs","");
        String message = "Hello World";
        for (int i = 0 ; i < 10 ;i++){
            message += ".";
            //4.将消息发布到绑定的队列中，
            channel.basicPublish("logs", "", null,message.getBytes());
            System.out.println("Send[x] Sent '" + message + "'");
        }
    }
}
