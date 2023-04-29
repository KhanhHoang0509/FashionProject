package com.fashion.client.customer;

import com.fashion.fashioncommon.entity.AuthenticationType;
import com.fashion.fashioncommon.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	Customer findByEmail(String email);

	@Query("select c from Customer c where c.verificationCode = ?1")
	Customer findByVerificationCode(String Code);

	@Query("update Customer c set c.enabled = true, c.verificationCode = null where c.id = ?1")
	@Modifying
	void enable(Integer id);

	@Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
	@Modifying
    void updateAuthenticationType(Integer id, AuthenticationType type);

	Customer findByResetPasswordToken(String token);
}


