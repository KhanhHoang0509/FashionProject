package com.fashion.client.setting;

import com.fashion.fashioncommon.entity.Country;
import com.fashion.fashioncommon.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);
}
