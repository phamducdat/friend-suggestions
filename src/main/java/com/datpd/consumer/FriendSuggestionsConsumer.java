package com.datpd.consumer;

import com.datpd.service.ContactPhoneNumberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FriendSuggestionsConsumer implements AcknowledgingMessageListener<Long, String> {

    private final ContactPhoneNumberService contactPhoneNumberService;

    public FriendSuggestionsConsumer(ContactPhoneNumberService contactPhoneNumberService) {
        this.contactPhoneNumberService = contactPhoneNumberService;
    }


    @Override
    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void onMessage(ConsumerRecord<Long, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("consumed: {}", consumerRecord);
        try {
            contactPhoneNumberService.processContactPhoneNumbers(consumerRecord);
            assert acknowledgment != null;
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            log.info("Error when Json processing: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
