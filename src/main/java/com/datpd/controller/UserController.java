package com.datpd.controller;

import com.datpd.dto.ContactPhoneNumberDto;
import com.datpd.dto.FriendDto;
import com.datpd.dto.UserDto;
import com.datpd.service.ContactPhoneNumberService;
import com.datpd.service.FriendService;
import com.datpd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ContactPhoneNumberService contactPhoneNumberService;

    private final FriendService friendService;

    public UserController(UserService userService, ContactPhoneNumberService contactPhoneNumberService, FriendService friendService) {
        this.userService = userService;
        this.contactPhoneNumberService = contactPhoneNumberService;
        this.friendService = friendService;
    }

    @GetMapping("/{userId}")
    public UserDto getUserByUserId(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/contact-phone-numbers")
    public List<ContactPhoneNumberDto> getContactPhoneNumbersByUserId(@PathVariable long userId) {
        return contactPhoneNumberService.getAllContactPhoneNumberByUserId(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<FriendDto> getFriendsByUserId(@PathVariable long userId) {
        return friendService.getAllFriendsByUserId(userId);
    }
}
