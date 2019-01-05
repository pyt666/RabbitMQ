package com.rabbit.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ClassName Worker2
 * @Author pyt
 * @Date 2019/1/5 15:25
 */
public class Worker2 {
    private final static String QUEUE_NAME = "hello";



    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println("[*] Waiting for message. To exit press CTRL + C");

        DeliverCallback deliverCallback = (consumerTag, deliver) -> {
            String message = new String(deliver.getBody(),"UTF-8");
            System.out.println("[x] Received '" + message + "'");
            long start = System.currentTimeMillis();
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }finally {
                long end = System.currentTimeMillis();
                System.out.println((end-start)/1000+"s");
                System.out.println("[x] Done");
            }
        };
        boolean autoAck = true;//自动确认，客户端关闭后，处理中以及将分配的task都将lose
        channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,consumerTag->{});
    }

    private static void doWork(String task) throws InterruptedException{
        for(char ch : task.toCharArray()){
            if(ch == '.')
                Thread.sleep(1000);
        }
    }
}
