package com.rabbit.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ClassName Worker3
 * @Author pyt
 * @Date 2019/1/5 17:10
 */
public class Worker3 {
    private final static String QUEUE_NAME = "task_queue";
    public static void main(String[] args) throws Exception{
        //1.创建服务器连接
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器主机地址
        factory.setHost("localhost");
        //2.建立连接和频道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //3.在频道中声明队列
        boolean durable = true ;
        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
        System.out.println("[*] Waiting for message. To exit press CTRL + C");

        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

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
                //4.信息确认结果
                channel.basicAck(deliver.getEnvelope().getDeliveryTag(),false);
            }
        };
        //默认打开手动确认
        boolean autoAck = false;//手动确认信息
        channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,consumerTag->{});
    }

    private static void doWork(String task) throws InterruptedException{
        for(char ch : task.toCharArray()){
            if(ch == '.')
                Thread.sleep(1000);
        }
    }
}
