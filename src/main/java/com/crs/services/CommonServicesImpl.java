package com.crs.services;

import java.util.List;
import java.util.Random;

import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class CommonServicesImpl implements ICommonServices {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${localCreated.kafka.topic}")
	private String kafkaTopic;

	@Override
	public Boolean sendDataToKafkaBroker(String message) {
		try {
			int partition = getPartionValue(kafkaTopic);
			String messageKey = "CentralReservationsSystem";
			logger.info("Sending data to kafka topic : {}, partition : {}, messageKey : {}, message : {}", kafkaTopic,
					partition, messageKey, message);
			kafkaTemplate.send(kafkaTopic, partition, messageKey, message);
			return true;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
			return false;
		}

	}

	private int getPartionValue(String topicName) {
		try {
			List<PartitionInfo> partition = kafkaTemplate.partitionsFor(topicName);
			if (partition != null && partition.size() > 0) {
				return new Random().nextInt(partition.size());
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
