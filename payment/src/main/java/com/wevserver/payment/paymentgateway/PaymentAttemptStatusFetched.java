package com.wevserver.payment.paymentgateway;

import com.wevserver.payment.paymentattempt.PaymentAttemptStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentAttemptStatusFetched {

    private PaymentAttemptStatus attemptStatus;
}
