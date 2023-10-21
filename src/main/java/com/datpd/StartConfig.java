package com.datpd;

import com.datpd.entity.UserEntity;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StartConfig {

    private final UserRepository userRepository;
    private final ContactPhoneNumberRepository contactPhoneNumberRepository;

    public StartConfig(UserRepository userRepository,
                       ContactPhoneNumberRepository contactPhoneNumberRepository) {
        this.userRepository = userRepository;
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
    }

    @Bean
    public CommandLineRunner initDatabase() {

        // check if user_entity table is empty, then add a user
        return args -> {
            if (userRepository.count() == 0) {
                log.info("Init data to user_entity table");
                UserEntity userEntity0 = UserEntity.builder()
                        .name("John")
                        .primaryPhoneNumber("0012345678")
                        .build();
                UserEntity userEntity1 = UserEntity.builder()
                        .name("Terry")
                        .primaryPhoneNumber("1012345678")
                        .build();
                UserEntity userEntity2 = UserEntity.builder()
                        .name("Wayne")
                        .primaryPhoneNumber("2012345678")
                        .build();
                UserEntity userEntity3 = UserEntity.builder()
                        .name("Rooney")
                        .primaryPhoneNumber("3012345678")
                        .build();
                userRepository.save(userEntity0);
                userRepository.save(userEntity1);
                userRepository.save(userEntity2);
                userRepository.save(userEntity3);
            }

            // check if contact_phone_number_entity is empty, then add data
//        if (contactPhoneNumberRepository.count() == 0) {
//            log.info("Init data to contact_phone_number_entity table");
//            List<String> ContactPhoneNumbers0 = List.of(
//                    "1012345678",
//                    "1012345678",
//                    "3012345678"
//            );
//            List<String> ContactPhoneNumbers1 = List.of("0012345678");
//            List<String> ContactPhoneNumbers2 = List.of("0012345678");
//            List<String> ContactPhoneNumbers3 = new ArrayList<>();
//        }
        };
    }
}
