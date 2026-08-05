package com.example.mylog.dto;

import lombok.Data;

/* =========================
   天気情報DTO
   天気予報データを保持
   ========================= */
@Data
public class WeatherDto {

	/* 地域名 */
	private String area;

	/* 今日の天気 */
	private String todayWeather;

	/* 明日の天気 */
	private String tomorrowWeather;

	/* 明後日の天気 */
	private String afterTomorrowWeather;

}
