package com.fashion.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) { //muon dung photo phai dung addResourceHandlers cap quyen truy cap cho user
		exposeDirectory("user-photos", registry); //user-photos ten folder
		exposeDirectory("../category-images", registry);
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../product-images", registry);

//		registry.addResourceHandler("/brand-logos/").addResourceLocations("/brand-logos/");
	}

	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);

		String absolutePath = path.toFile().getAbsolutePath();

		//String logicalPath = pathPattern.replace("../","") + "/**";//user-photos/**
		String logicalPath = pathPattern.replace("../", "") + "/**";//user-photos/**, /** la nhung gi ben trong user-photos dc truy cap

		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
	}
}
