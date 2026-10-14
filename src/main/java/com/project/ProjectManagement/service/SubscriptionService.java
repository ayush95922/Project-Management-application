package com.project.ProjectManagement.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.PlanType;
import com.project.ProjectManagement.model.Subscription;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.SubscriptionRepo;

@Service
public class SubscriptionService {
    @Autowired
    SubscriptionRepo subscriptionRepo;

    @Autowired
    UserService userService;

    public Subscription createSubscription(User user) {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setIsValid(true);
        subscription.setPlanType(PlanType.FREE);
        subscription.setUser(user);

        return subscriptionRepo.save(subscription);
    }

    public Subscription getUserSubscription(Long userId)
    {
        Subscription subscription = subscriptionRepo.findByUserId(userId);
        // Whenever user fetches the subscription plan, it checks if the plan is valid(according to current date)
        // if it is not valid, then reset it to free plan
        if (!isValid(subscription))
        {
            subscription.setPlanType(PlanType.FREE);
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
            subscription.setSubscriptionStartDate(LocalDate.now());
        }
        return subscriptionRepo.save(subscription);
    }

    public Subscription upgradeSubscription(Long userId, PlanType planType)
    {
        Subscription subscription = subscriptionRepo.findByUserId(userId);
        subscription.setPlanType(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());
        if (planType.equals(PlanType.ANNUALLY)) subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        else if (planType.equals(PlanType.MONTHLY)) subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
        return subscriptionRepo.save(subscription);
    }

    public Boolean isValid(Subscription subscription)
    {
        if (subscription.getPlanType().equals(PlanType.FREE)) return true;
        LocalDate endDate = subscription.getSubscriptionEndDate();
        LocalDate currentDate = LocalDate.now();

        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
    }
}
