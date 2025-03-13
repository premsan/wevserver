package com.wevserver.payment.paymentattempt;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import com.wevserver.payment.payment.Payment;
import com.wevserver.payment.payment.PaymentRepository;
import com.wevserver.payment.paymentgateway.PaymentAttemptCreated;
import com.wevserver.payment.paymentgateway.PaymentGateway;
import com.wevserver.payment.paymentgateway.PaymentGatewayRepository;
import jakarta.validation.Valid;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PaymentAttemptCreateController {

    private final PaymentRepository paymentRepository;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final PaymentGatewayRepository paymentGatewayRepository;

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = PaymentAttempt.class)
    @GetMapping("/payment/payment-attempt-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PAYMENT_PAYMENT_ATTEMPT_CREATE')")
    public ModelAndView getPaymentAttemptCreate() {

        final ModelAndView model =
                new ModelAndView("com/wevserver/payment/templates/payment-attempt-create");

        model.addObject(
                "gatewayIds", paymentGatewayRepository.findAll().stream().map(PaymentGateway::id));
        model.addObject("paymentAttemptCreate", new PaymentAttemptCreate());

        return model;
    }

    @PostMapping("/payment/payment-attempt-create")
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or hasAuthority('PAYMENT_PAYMENT_ATTEMPT_CREATE') or"
                    + " hasPermission(#paymentAttemptCreate, 'paymentAttemptCreate')")
    public ModelAndView postPaymentAttemptCreate(
            @Valid @ModelAttribute("paymentAttemptCreate")
                    final PaymentAttemptCreate paymentAttemptCreate,
            final BindingResult bindingResult,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/payment/templates/payment-attempt-create");
            modelAndView.addObject(
                    "gatewayIds",
                    paymentGatewayRepository.findAll().stream().map(PaymentGateway::id));
            modelAndView.addObject("paymentAttemptCreate", paymentAttemptCreate);

            return modelAndView;
        }

        final PaymentGateway paymentGateway =
                paymentGatewayRepository.findById(paymentAttemptCreate.getGatewayId());

        if (paymentGateway == null) {

            bindingResult.rejectValue("gatewayId", "Invalid gatewayId");

            modelAndView.setViewName("com/wevserver/payment/templates/payment-attempt-create");
            modelAndView.addObject(
                    "gatewayIds",
                    paymentGatewayRepository.findAll().stream().map(PaymentGateway::id));
            modelAndView.addObject("paymentAttemptCreate", paymentAttemptCreate);

            return modelAndView;
        }

        final Optional<Payment> optionalPayment =
                paymentRepository.findById(paymentAttemptCreate.getPaymentId());

        if (optionalPayment.isEmpty()) {

            bindingResult.rejectValue("paymentId", "Invalid paymentId");

            modelAndView.setViewName("com/wevserver/payment/templates/payment-attempt-create");
            modelAndView.addObject(
                    "gatewayIds",
                    paymentGatewayRepository.findAll().stream().map(PaymentGateway::id));
            modelAndView.addObject("paymentAttemptCreate", paymentAttemptCreate);

            return modelAndView;
        }

        final String attemptId = UUID.randomUUID().toString();
        final com.wevserver.payment.paymentgateway.PaymentAttemptCreate
                paymentGatewayPaymentAttemptCreate =
                        new com.wevserver.payment.paymentgateway.PaymentAttemptCreate();
        paymentGatewayPaymentAttemptCreate.setReferenceId(attemptId);
        paymentGatewayPaymentAttemptCreate.setAmount(optionalPayment.get().getAmount());
        paymentGatewayPaymentAttemptCreate.setCurrency(
                Currency.getInstance(optionalPayment.get().getCurrency()));

        final PaymentAttemptCreated paymentAttemptCreated =
                paymentGateway.paymentAttemptCreate(paymentGatewayPaymentAttemptCreate);

        final PaymentAttempt paymentAttempt =
                paymentAttemptRepository.save(
                        new PaymentAttempt(
                                attemptId,
                                null,
                                paymentAttemptCreate.getPaymentId(),
                                paymentAttemptCreate.getGatewayId(),
                                paymentAttemptCreated.getId(),
                                paymentAttemptCreated.getUrl(),
                                null,
                                null,
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        return new ModelAndView("redirect:" + paymentAttempt.getGatewayAttemptUrl());
    }

    @Getter
    @Setter
    public static class PaymentAttemptCreate {

        private String paymentId;

        private String gatewayId;
    }
}
