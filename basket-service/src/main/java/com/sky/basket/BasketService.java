package com.sky.basket;

import com.sky.customer.CustomerService;
import com.sky.subscription.SubscriptionService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BasketService {


    private final SubscriptionService subscriptionService;
    private final CustomerService customerService;


    public BasketService(SubscriptionService subscriptionService, CustomerService customerService) {
        this.subscriptionService = subscriptionService;
        this.customerService = customerService;
    }

    public BigDecimal calculate(List<String> subscriptions) throws BasketConditionNotMetException {


        var totalPriceOfSubscriptions = subscriptions.stream()
                .map(e -> Optional.ofNullable(subscriptionService.getSubscriptionPrice(e))
                        .orElseThrow(() -> new BasketConditionNotMetException()))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        log.info("total price is : " + totalPriceOfSubscriptions);
        return totalPriceOfSubscriptions;
    }


    public BigDecimal calculateWithCostumerId(List<String> subscriptions, String costumerId) throws BasketConditionNotMetException {

        var listOfSubs = Optional.ofNullable(customerService.getSubscriptionsForCustomer(costumerId))
                .orElseThrow(() -> new BasketConditionNotMetException());

        var priceOfNonExistSubs = subscriptions.stream()
                .filter(e -> !listOfSubs.contains(e))
                .map(e -> Optional.ofNullable(subscriptionService.getSubscriptionPrice(e))
                        .orElseThrow(() -> new BasketConditionNotMetException()))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        log.info("price of non exist subs : " + priceOfNonExistSubs);
        return priceOfNonExistSubs;


    }
}
