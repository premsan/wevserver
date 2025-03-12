package com.wevserver.payment.payment;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository
        extends AuditableRepository<Payment>, CrudRepository<Payment, String> {

    @Override
    default Class<Payment> entityClass() {

        return Payment.class;
    }
}
