# kafka
sh kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --group test_grp --consumer-property enable.auto.commit=true --from-beginning



生产：sh kafka-console-producer.sh --broker-list localhost:9092 --topic test
 
 
 
nohup  bin/kafka-server-start.sh config/server.properties 1>/dev/null 2>&1 &

sh kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --group test_grp-1 --consumer-property enable.auto.commit=true --from-beginning
sh kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --group test_grp --consumer-property enable.auto.commit=true --from-beginning

Kafka 启动报错java.io.IOException: Can't resolve address.

不能解析VM_0_9_centos
kafka 连接原理
首先连接 192.168.0.141:9092
再连接返回的host.name = VM_0_9_centos,
最后继续连接advertised.host.name=VM_0_9_centos
解决办法
添加window解析
hosts 文件增加 
192.168.0.141 VM_0_9_centos
用cmd ping VM_0_9_centos 试试如果可以ping通即可。
