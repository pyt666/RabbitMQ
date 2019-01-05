package com.rabbit.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @ClassName Resv
 * @Author pyt
 * @Date 2019/1/5 11:54
 */
public class Resv {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println("[*] Waiting for message. To exit press CTRL + C");

        DeliverCallback deliverCallback = (consumerTag,deliver) -> {
            String message = new String(deliver.getBody(),"UTF-8");
            System.out.println("[x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag->{});
        /*Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("C [x] Received '" + message + "'");
            }
        };
//		自动回复队列应答 -- RabbitMQ中的消息确认机制，后面章节会详细讲解
        channel.basicConsume(QUEUE_NAME, true, consumer);*/
    }
}
