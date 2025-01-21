# mq-demo

查看當前kafka中所有的topic
- docker exec -it kafka kafka-topics.sh --list --bootstrap-server localhost:9092
  監聽topic 是否有資料近來
- docker exec -it kafka kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic [topic1-dlt] --from-beginning
