package com.datpd.controller;

import com.datpd.dto.ContactPhoneNumberDto;
import com.datpd.dto.FriendDto;
import com.datpd.dto.FriendSuggestionDto;
import com.datpd.dto.UserDto;
import com.datpd.service.ContactPhoneNumberService;
import com.datpd.service.FriendService;
import com.datpd.service.FriendSuggestionService;
import com.datpd.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

    public UserController(UserService userService,
                          ContactPhoneNumberService contactPhoneNumberService,
                          FriendService friendService,
                          FriendSuggestionService friendSuggestionService) {
        this.userService = userService;
        this.contactPhoneNumberService = contactPhoneNumberService;
        this.friendService = friendService;
        this.friendSuggestionService = friendSuggestionService;
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserByUserId(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/contact-phone-numbers")
    public List<ContactPhoneNumberDto> getContactPhoneNumbersByUserId(@PathVariable long userId) {
        return contactPhoneNumberService.getAllContactPhoneNumberByUserId(userId);
    }

    @PutMapping("/{userId}/contact-phone-number")
    public void updateContactPhoneNumbersByUserId(@PathVariable long userId,
                                                  @RequestBody List<ContactPhoneNumberDto> contactPhoneNumberDtoList) {
        contactPhoneNumberService.updateContactPhoneNumbersByUserId(userId, contactPhoneNumberDtoList);
    }

    @GetMapping("/{userId}/friends")
    public List<FriendDto> getFriendsByUserId(@PathVariable long userId) {
        return friendService.getAllFriendsByUserId(userId);
    }

    @GetMapping("/{userId}/friend-suggestions")
    public List<FriendSuggestionDto> getFriendSuggestionsByUserId(@PathVariable long userId) {
        return friendSuggestionService.getFriendSuggestionsByUserId(userId);
    }

    @DeleteMapping("/{userId}/friends")
    public void reset(@PathVariable long userId) {
        friendService.resetFriends(userId);
    }

}
