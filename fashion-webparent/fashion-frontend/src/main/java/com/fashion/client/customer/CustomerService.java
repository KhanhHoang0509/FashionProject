package com.fashion.client.customer;

import com.fashion.client.setting.CountryRepository;
import com.fashion.fashioncommon.entity.AuthenticationType;
import com.fashion.fashioncommon.entity.Country;
import com.fashion.fashioncommon.entity.Customer;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class CustomerService {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}

	public void registerCustomer(Customer customer) {
		encodePassword(customer);//mã hoá pass
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		String randomCode = RandomString.make(64);//tạo chuỗi ngẫu nhiên 64 kí tự
		customer.setVerificationCode(randomCode);//gán chuỗi trên vào setVer

		customerRepository.save(customer);
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepository.updateAuthenticationType(customer.getId(), type);
		}
	}

	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepository.enable(customer.getId());
			return true;
		}
	}

    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {

		Customer customer = customerRepository.findByEmail(email);

		if (customer != null) {
			String token = RandomString.make(30);//tạo ra chuỗi ngẫu nhiên gồm 30 ký tự
			customer.setResetPasswordToken(token);
			customerRepository.save(customer);

			return token;
		} else {
			throw new CustomerNotFoundException("Could not find any customer with the email " + email);
		}
    }

	public Customer getByResetPasswordToken(String token) {
		return customerRepository.findByResetPasswordToken(token);
	}

	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException{
		Customer customer = customerRepository.findByResetPasswordToken(token);

		if (customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}

		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);

        encodePassword(customer);
		customerRepository.save(customer);
	}

    public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
    }

    public void update(Customer customer) {
		Customer customerInDB = customerRepository.findById(customer.getId()).get();

		if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			if (!customer.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(customer.getPassword());
				customer.setPassword(encodedPassword);
			} else {
				customer.setPassword(customer.getPassword());
			}
		} else {
			customer.setPassword(customer.getPassword());
		}

		customer.setEnabled(customerInDB.isEnabled());
		customer.setCreatedTime(customerInDB.getCreatedTime());
		customer.setVerificationCode(customerInDB.getVerificationCode());
		customer.setAuthenticationType(customerInDB.getAuthenticationType());
		customer.setResetPasswordToken(customerInDB.getResetPasswordToken());

		customerRepository.save(customer);
    }

	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer == null;
	}
}
