package com.kafka.partitions;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @Author: Damon
 * @Date: 2020/5/7 22:19
 */
public class kafkaConsum {
    public static void main(String[] args) {
        // 1、准备配置文件

        Properties props = new Properties();

        props.put("bootstrap.servers", "cdh-datanode1:9092,cdh-datanode2:9092,cdh-datanode3:9092");

        props.put("group.id", "test");

        props.put("enable.auto.commit", "true");

        props.put("auto.commit.interval.ms", "1000");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");



        // 2、创建KafkaConsumer

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);

        // 3、订阅数据，这里的topic可以是多个

        kafkaConsumer.subscribe(Arrays.asList("yun01"));

        // 4、获取数据

        while (true) {

            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {

                System.out.printf("topic = %s,offset = %d, key = %s, value = %s%n",record.topic(), record.offset(), record.key(), record.value());

            }



        }
    }
}
