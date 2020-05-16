package com.kafka.partitions;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/**
 * @Author: Damon
 * @Date: 2020/5/7 22:18
 */
public class kafkaProducer {
    public static void main(String[] args) {
        //1、准备配置文件

        Properties props = new Properties();

        props.put("bootstrap.servers", "cdh-datanode1:9092,cdh-datanode2:9092");
        //0,1,all
        props.put("acks", "all");
        //重试次数：不必处理重试错误
        //可以通过retry.backoff.ms 参数来改变这个时间间隔。建议在设置重试次数和重试时间间隔之前，
        //先测试一下恢复一个崩愤节点需要多少时间（比如所有分区选举出首领需要多长时间），
        //让总的重试时间比 Kafka 集群从崩愤中恢复的时间长
        props.put("retries", 0);
//批次设置得很大，不会造成延迟，只会占用更多内存，设置太小，生产者发送消息更频繁，会增加额外开销
        props.put("batch.size", 16384);
//发送批次之前等待更多消息加入批次得时间，增加延迟，提高吞吐量
        props.put("linger.ms", 1);
//
        //该参数用来设置生产者内存缓冲区的大小，生产者用它缓冲要发送到服务器的消息。如果
        //应用程序发送消息的速度超过发送到服务器的速度，会导致生产者空间不足。这个 候，
        //send （）方法调用要么被阻塞，要么抛出异常，取决于如何设置 block.on.buffe 「. full 参数
        //（在 0. 9.0.0 版本里被替换成了 l'la .block.l'ls ，表示在抛出异常之前可以阻塞一段时间）。
        props.put("buffer.memory", 33554432);
        //snappy:占用较少cpu，能提供较好的性能和相当可观的压缩比，关注性能和网络带宽推荐
        //gzip:占用较多cpu，能提供更高的压缩比，网络带宽有限推荐
        //使用压缩可以降低网络传输开销和存储开销
        props.put("compression.type","snappy");

        //重要参数，必须
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //重要参数，必须
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //任意字符串，服务器用它来识别消息来源，还可以用在日志和配额指标里
        props.put("client.id","1");
        //自定义分区
        props.put("partitioner.class", "com.kafka.partitions.myPartitioner");
        //2、创建KafkaProducer

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);
        //发送消息3种方式
        ProducerRecord<String,String> record = new ProducerRecord<>("CustomerCountry","A Precision Products","France");
        //1.发送并忘记
        try {
            kafkaProducer.send(record);
        }catch (Exception e){
            e.printStackTrace();
        }

        //2.同步发送
        try {
            RecordMetadata recordMetadata = kafkaProducer.send(record).get();
            System.out.println(recordMetadata.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        //3.异步发送

        try {
            kafkaProducer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e != null){
                        e.printStackTrace();
                    }else {
                        System.out.println(recordMetadata.toString());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


//        for (int i=0;i<100;i++){
//            //3、发送数据
//            kafkaProducer.send(new ProducerRecord<String, String>("yun01","num"+i,"value"+i));
//        }

        kafkaProducer.close();
    }


}
