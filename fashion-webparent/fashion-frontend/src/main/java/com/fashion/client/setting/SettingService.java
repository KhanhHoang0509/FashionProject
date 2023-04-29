package com.fashion.client.setting;


import com.fashion.fashioncommon.entity.setting.Setting;
import com.fashion.fashioncommon.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepository;
	public EmailSettingBag getEmailSettings() {
		List<Setting> settingList = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);

		return new EmailSettingBag(settingList);
	}
}
