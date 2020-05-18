package com.kafka.ss;

/**
 * @Author: Damon
 * @Date: 2020/2/27 17:38
 */
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


public class Producer
{
    private static final String TOPIC="education-info";
    private static final String BROKER_LIST="cdh-datanode1:9092,cdh-datanode2:9092,cdh-datanode3:9092";
    private static KafkaProducer<String,String> producer = null;

    static{
        Properties configs = initConfig();
        producer = new KafkaProducer<String, String>(configs);
    }

    private static Properties initConfig(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKER_LIST);
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args){
        try{
            String message = "hello world";
            ProducerRecord<String,String> record = new ProducerRecord<String,String>(TOPIC,message);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if(null==exception){
                        System.out.println("perfect!");
                    }
                    if(null!=metadata){
                        System.out.print("offset:"+metadata.offset()+";partition:"+metadata.partition());
                    }
                }
            }).get();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }
    }
}