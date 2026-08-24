package com.example.mylog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	/* Spring Securityのセキュリティ設定 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		/* URLごとのアクセス権限を設定 */
		http.authorizeHttpRequests(auth -> auth
				/* ログ関連の画面はログインなしでアクセス可能 */
				.requestMatchers("/logs/**").permitAll()
				/* その他の画面はログインが必要 */
				.anyRequest().authenticated());
		/* 設定したセキュリティ設定を適用 */
		return http.build();
	}
}
