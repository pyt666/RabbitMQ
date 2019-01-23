package com.rabbit.routing;

import com.rabbitmq.client.*;

/**
 * @ClassName RecieveLogs_2
 * @Description TODO
 * @Author pyt
 * @Date 2019/1/18 18:28
 * @Version
 */
public class RecieveLogs_2 {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "warning");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
