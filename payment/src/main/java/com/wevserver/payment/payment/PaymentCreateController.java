package com.wevserver.payment.payment;

import com.nimbusds.jwt.SignedJWT;
import com.wevserver.api.PaymentCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.security.sign.SignedToken;
import jakarta.validation.Valid;
import java.util.Currency;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PaymentCreateController {

    private final PaymentRepository paymentRepository;

    @GetMapping(PaymentCreate.PATH)
    @FeatureMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PAYMENT_PAYMENT_CREATE')")
    public ModelAndView getPaymentCreate(
            final @SignedToken SignedJWT signedToken,
            final PaymentCreate.RequestParameters requestParameters) {

        final ModelAndView model =
                new ModelAndView("com/wevserver/payment/templates/payment-create");
        model.addObject(
                "CurrencyCode",
                Currency.getAvailableCurrencies().stream()
                        .map(Currency::getCurrencyCode)
                        .sorted(String::compareTo));
        model.addObject("paymentCreate", requestParameters);

        return model;
    }

    @PostMapping(PaymentCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('PAYMENT_PAYMENT_CREATE')")
    public ModelAndView postPaymentCreate(
            @Valid @ModelAttribute("paymentCreate")
                    final PaymentCreate.RequestParameters paymentCreate,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/payment/templates/payment-create");
            modelAndView.addObject("paymentCreate", paymentCreate);

            return modelAndView;
        }

        final Payment payment =
                paymentRepository.save(
                        new Payment(
                                UUID.randomUUID().toString(),
                                null,
                                paymentCreate.getReferenceId(),
                                paymentCreate.getCurrency().getCurrencyCode(),
                                paymentCreate.getAmount(),
                                paymentCreate.getName(),
                                paymentCreate.getDescription(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", payment.getId());
        return new ModelAndView("redirect:/payment/payment-view/{id}");
    }
}
