package com.sky.basket;


import com.sky.customer.CustomerService;
import com.sky.subscription.SubscriptionService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class BasketServiceTest {


    @InjectMocks
    private BasketService basketService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private CustomerService customerService;


    static String ENTERTAINMENT;
    static String SPORTS;
    static String KID;
    static String MOVIE;
    static String BOOST;
    static String CUSTOMERID;


    static {
        ENTERTAINMENT = "ENTERTAINMENT";
        SPORTS = "SPORTS";
        KID = "KID";
        MOVIE = "MOVIE";
        BOOST = "BOOST";
        CUSTOMERID = "10";
    }



/**
    Scenario: Successful basket calculation of a single subscription
    Given the customer wants to purchase an ENTERTAINMENT subscription
    When the basket is calculated
    Then a successful response is returned with £9.99 as the charge
*/

    @Test
    @DisplayName("Given valid subsciption expect valid price")
    public void given_valid_dub_expect_valid_price() {

        Mockito.when(subscriptionService.getSubscriptionPrice(ENTERTAINMENT))
                .thenReturn(BigDecimal.valueOf(9.99));
        var price = basketService.calculate(List.of(ENTERTAINMENT));

        Mockito.verify(subscriptionService).getSubscriptionPrice(ENTERTAINMENT);
        Assertions.assertThat(price).isEqualTo(BigDecimal.valueOf(9.99));

    }




    /**
    Scenario: Calculation of an unknown subscription
    Given the customer wants to purchase a MOVIES subscription
    And the SubscriptionService does not return a price (returns null)
    When the basket is calculated
    Then a BasketConditionNotMetException is thrown
*/

    @Test
    @DisplayName("Given invalid subscroiption type expect BasketConditionNotMetException")
    public void given_invalid_subType_expect_exception() {

        Mockito.when(subscriptionService
                .getSubscriptionPrice(MOVIE))
                .thenReturn(null);

        Assertions.assertThatExceptionOfType(BasketConditionNotMetException.class)
                .isThrownBy(() -> basketService.calculate(List.of(MOVIE)));
        Mockito.verify(subscriptionService).getSubscriptionPrice(eq(MOVIE));

    }





    /**
    Scenario: Successful basket calculation of multiple subscriptions
    Given the customer wants to purchase ENTERTAINMENT and SPORTS subscriptions
    When the basket is calculated
    Then a successful response is returned with £29.98 as the charge

*/

    @Test
    @DisplayName("Given invalid multiple subsciptions return sum of subsciptions")
    public void given_valid_multiple_subsciptions_expect_sum_of_allservices() {

        Mockito.when(subscriptionService.getSubscriptionPrice(ENTERTAINMENT))
                .thenReturn(BigDecimal.valueOf(9.99));
        Mockito.when(subscriptionService.getSubscriptionPrice(SPORTS))
                .thenReturn(BigDecimal.valueOf(19.99));

        var price = basketService.calculate(List.of(ENTERTAINMENT, SPORTS));

        Mockito.verify(subscriptionService).getSubscriptionPrice(ENTERTAINMENT);
        Mockito.verify(subscriptionService).getSubscriptionPrice(SPORTS);

        Assertions.assertThat(price).isEqualTo(BigDecimal.valueOf(29.98));

    }




/**
   Scenario: Basket condition of existing subscription for BOOST purchase not met
    Given the customer does not have any existing subscriptions
    And the customer wants to purchase a BOOST subscription
    When the basket is calculated
    Then a BasketConditionNotMetException exception is thrown
 */

    @Test
    @DisplayName("Given not exist subciption of Customer expect BasketConditionNotMetException expection")
    public void given_nonExist_sub_expect_exception() {

        Mockito.when(customerService.getSubscriptionsForCustomer(CUSTOMERID))
                .thenReturn(null);
        Assertions.assertThatExceptionOfType(BasketConditionNotMetException.class)
                .isThrownBy(() -> basketService.calculateWithCostumerId(List.of(BOOST), CUSTOMERID));


    }


/**
    Scenario: Successful basket calculation of a BOOST subscription
    Given the customer has an existing ENTERTAINMENT subscription
    And the customer wants to purchase a BOOST subscription
    When the basket is calculated
    Then a successful response is returned with £1.99 as the charge
 */

    @Test
    @DisplayName("Given not exist subciption of Customer expect BasketConditionNotMetException expection")
    public void given_costumer_has_existingone_entertainment_then() {

        Mockito.when(customerService.getSubscriptionsForCustomer(CUSTOMERID))
                .thenReturn(null);
        Assertions.assertThatExceptionOfType(BasketConditionNotMetException.class)
                .isThrownBy(() -> basketService.calculateWithCostumerId(List.of(BOOST), CUSTOMERID));


    }
}