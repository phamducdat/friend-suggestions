package com.datpd.controller;

import com.datpd.domain.ContactPhoneNumbersDomain;
import com.datpd.dto.ContactPhoneNumberDto;
import com.datpd.dto.FriendDto;
import com.datpd.dto.FriendSuggestionDto;
import com.datpd.dto.UserDto;
import com.datpd.producer.FriendSuggestionsProducer;
import com.datpd.service.ContactPhoneNumberService;
import com.datpd.service.FriendService;
import com.datpd.service.FriendSuggestionService;
import com.datpd.service.UserService;
import com.datpd.utils.ApiTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ContactPhoneNumberService contactPhoneNumberService;

    private final FriendService friendService;

    private final FriendSuggestionService friendSuggestionService;

    private final FriendSuggestionsProducer friendSuggestionsProducer;

    public UserController(UserService userService,
                          ContactPhoneNumberService contactPhoneNumberService,
                          FriendService friendService,
                          FriendSuggestionService friendSuggestionService,
                          FriendSuggestionsProducer friendSuggestionsProducer) {
        this.userService = userService;
        this.contactPhoneNumberService = contactPhoneNumberService;
        this.friendService = friendService;
        this.friendSuggestionService = friendSuggestionService;
        this.friendSuggestionsProducer = friendSuggestionsProducer;
    }

    @GetMapping()
    public Page<UserDto> getUsers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        return userService.getUsers(page, pageSize);
    }

    @GetMapping("/{userId}")
    public UserDto getUserByUserId(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/contact-phone-numbers")
    public Page<ContactPhoneNumberDto> getContactPhoneNumbersByUserId(@PathVariable long userId,
                                                                      @RequestParam(name = "page", defaultValue = "1") int page,
                                                                      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        return contactPhoneNumberService.getAllContactPhoneNumberByUserId(userId, page, pageSize);
    }

    @PutMapping("/{userId}/contact-phone-number")
    public void updateContactPhoneNumbersByUserId(@PathVariable long userId,
                                                  @RequestBody List<ContactPhoneNumberDto> contactPhoneNumberDtoList)
            throws JsonProcessingException {
        ContactPhoneNumbersDomain contactPhoneNumbersDomain = ContactPhoneNumbersDomain.builder()
                .userId(userId)
                .contactPhoneNumberDtoList(contactPhoneNumberDtoList)
                .apiType(ApiTypeEnum.UPDATE)
                .build();
        friendSuggestionsProducer.sendContactPhoneNumbers(contactPhoneNumbersDomain);
    }

    @GetMapping("/{userId}/friends")
    public List<FriendDto> getFriendsByUserId(@PathVariable long userId,
                                              @RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return friendService.getAllFriendsByUserId(userId);
    }

    @GetMapping("/{userId}/friend-suggestions")
    public List<FriendSuggestionDto> getFriendSuggestionsByUserId(@PathVariable long userId,
                                                                  @RequestParam(name = "page", defaultValue = "1") int page,
                                                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return friendSuggestionService.getFriendSuggestionsByUserId(userId);
    }

    @DeleteMapping("/{userId}/friends")
    public void reset(@PathVariable long userId) {
        friendService.resetFriends(userId);
    }

}
