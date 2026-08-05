package com.example.mylog.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.mylog.service.WeatherService;


/* 
 * 全コントローラ共通処理
 * 各画面へ共通で渡すデータを管理
 */
@ControllerAdvice
public class GlobalControllerAdvice {

	/* 天気情報サービス */
	private final WeatherService weatherService;
	/* コンストラクタインジェクション */
	public GlobalControllerAdvice(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	/* 全画面へ天気情報を渡す */
	@ModelAttribute
	public void addWeather(Model model) {
		/* 天気情報をViewへ渡す */
		model.addAttribute("weather", weatherService.getWeather());
	}

}