package com.kafka.partitions;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * @Author: Damon
 * @Date: 2020/5/16 20:25
 */
public class myPartitioner implements Partitioner {


    @Override
    public void configure(Map<String, ?> map) {

    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        if(((keyBytes == null) || (!(key instanceof  String)))){
            throw new InvalidRecordException("we expect all messages to have customer name as key");
        }
        if((key).equals("Banana")){
            return numPartitions;
        }

        //其他记录被散列到其他分区
        return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions -1));
    }

    @Override
    public void close() {

    }

}
