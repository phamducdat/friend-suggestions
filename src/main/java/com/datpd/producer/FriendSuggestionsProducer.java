package com.datpd.producer;

import com.datpd.domain.ContactPhoneNumbersDomain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class FriendSuggestionsProducer {

    private final KafkaTemplate<Long, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public FriendSuggestionsProducer(KafkaTemplate<Long, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendContactPhoneNumbers(ContactPhoneNumbersDomain contactPhoneNumbersDomain) throws JsonProcessingException {
        Long key = contactPhoneNumbersDomain.getUserId();
        String value = objectMapper.writeValueAsString(contactPhoneNumbersDomain);

        ListenableFuture<SendResult<Long, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);

        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private void handleFailure(Long key, String value, Throwable ex) {
        log.error("Error Sending the Message with key: {}, value: {} and the exception is {}", key, value, ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Long key, String value, SendResult<Long, String> result) {
        log.info("Message Sent Successfully for the key: {} and the value is {}, partition is {}",
                key, value, result.getRecordMetadata().partition());
    }
}
