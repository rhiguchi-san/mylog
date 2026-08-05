package com.example.mylog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/* =========================
   アプリケーション設定クラス
   Beanの設定を管理
   ========================= */
@Configuration
public class AppConfig {

	/* RestTemplateをBeanとして登録する */
	@Bean
	public RestTemplate restTemplate() {

		/* RestTemplateのインスタンスを返す */
		return new RestTemplate();
	}
}
