package com.project.ProjectManagement.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ProjectManagement.model.PlanType;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.response.PaymentLinkResponse;
import com.project.ProjectManagement.service.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;

@RestController
@RequestMapping("/api/payemnt")
public class PaymentController {
    @Value("${razorpay.api.key}")
    private String api_key;

    @Value("${razorpay.api.secret}")
    private String api_secret;

    @Autowired
    private UserService userService;

    @PostMapping("/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable PlanType planType, @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserProfileByJWT(jwt);
        int amount = 799 * 100;
        if (planType.equals(PlanType.ANNUALLY))
        {
            amount *= 12;
            amount = (int) (amount * 0.7);
        }

        try
        {
            RazorpayClient razorpayClient = new RazorpayClient(api_key, api_secret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost:5173/upgrade_plan/success?planType" + planType);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentLinkResponse response = new PaymentLinkResponse(paymentLinkUrl, paymentLinkId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            throw new Exception("Payment failed due to " + e.getMessage());
        }
    }
}
