package com.datpd.config;

import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.entity.UserEntity;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class StartConfig {

    private final RedissonClient redissonClient;
    private final UserRepository userRepository;
    private final ContactPhoneNumberRepository contactPhoneNumberRepository;

    public StartConfig(RedissonClient redissonClient, UserRepository userRepository,
                       ContactPhoneNumberRepository contactPhoneNumberRepository) {
        this.redissonClient = redissonClient;
        this.userRepository = userRepository;
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
    }

    @Bean
    public CommandLineRunner initDatabase() {

        // check if user_entity table is empty, then add a user
        return args -> {
            // Reset Redis
            redissonClient.getKeys().flushdb();

            if (userRepository.count() == 0) {
                log.info("Init data to user_entity table");
                for (int i = 1; i <= 100; i++) {
                    UserEntity userEntity = UserEntity.builder()
                            .name("user" + i)
                            .primaryPhoneNumber(String.valueOf(i))
                            .build();
                    userRepository.save(userEntity);
                }
            }

            // check if contact_phone_number_entity is empty, then add data
            if (contactPhoneNumberRepository.count() == 0) {
                log.info("Init data to contact_phone_number_entity table");
                for (int i = 1; i <= 100; i++) {

                    UserEntity userEntity = userRepository.findByPrimaryPhoneNumber(String.valueOf(i));
                    Random random = new Random();
                    List<Integer> randomNumbers = new ArrayList<>();

                    for (int j = 1; j < 20; j++) {
                        int candidate = (100 + i + j) % 100;
                        randomNumbers.add(candidate);
                    }

                    contactPhoneNumberRepository.saveAll(randomNumbers.stream().map(
                            integer -> ContactPhoneNumberEntity.builder()
                                    .userId(userEntity.getId())
                                    .contactUserName("user" + integer)
                                    .contactPhoneNumber(String.valueOf(integer))
                                    .build()
                    ).collect(Collectors.toList()));
                }

            }
        };
    }
}
