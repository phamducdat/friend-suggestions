package com.datpd.consumer;

import com.datpd.service.ContactPhoneNumberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FriendSuggestionsConsumer {

    private final ContactPhoneNumberService contactPhoneNumberService;

    public FriendSuggestionsConsumer(ContactPhoneNumberService contactPhoneNumberService) {
        this.contactPhoneNumberService = contactPhoneNumberService;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(ConsumerRecord<Long, String> consumerRecord)
            throws JsonProcessingException {
        log.info("consumed: {}", consumerRecord);
        contactPhoneNumberService.processContactPhoneNumbers(consumerRecord);
    }
}
