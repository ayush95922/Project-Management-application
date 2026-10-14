package com.project.ProjectManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ProjectManagement.model.PlanType;
import com.project.ProjectManagement.model.Subscription;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.service.SubscriptionService;
import com.project.ProjectManagement.service.UserService;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    UserService userService;

    public ResponseEntity<Subscription> getUserSubscription(@RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserProfileByJWT(jwt);
        Subscription subscription = subscriptionService.getUserSubscription(user.getId());
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    @PatchMapping("/upgrade")
    public ResponseEntity<Subscription> upgradeUserSubscription(@RequestHeader("Authorization") String jwt, @RequestParam PlanType planType) throws Exception
    {
        User user = userService.findUserProfileByJWT(jwt);
        return new ResponseEntity<>(subscriptionService.upgradeSubscription(user.getId(), planType), HttpStatus.OK);
    }   
}
