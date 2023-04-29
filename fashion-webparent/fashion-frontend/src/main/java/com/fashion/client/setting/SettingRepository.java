package com.fashion.client.setting;


import com.fashion.fashioncommon.entity.setting.Setting;
import com.fashion.fashioncommon.entity.setting.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);
}
