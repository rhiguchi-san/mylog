package com.example.mylog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.mylog.dto.WeatherDto;
import com.example.mylog.dto.WeatherResponse;

/* =========================
   天気情報サービス
   天気予報APIから天気情報を取得
   ========================= */
@Service
public class WeatherService {

	/* HTTP通信を行うためのクラス */
	private final RestTemplate restTemplate;
	/* 天気予報APIのURL */
	private static final String URL = "https://weather.tsukumijima.net/api/forecast/city/270000";

	/* コンストラクタインジェクション */
	public WeatherService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/* 天気情報を取得する */
	public WeatherDto getWeather() {
		/* 天気予報APIから天気情報を取得 */
		WeatherResponse response = restTemplate.getForObject(URL, WeatherResponse.class);
		/* レスポンスまたは天気情報が取得できなかった場合 */
		if (response == null || response.getForecasts() == null) {
			return new WeatherDto();
		}
		/* 画面表示用DTOを作成 */
		WeatherDto dto = new WeatherDto();
		/* 地域名を設定 */
		dto.setArea(response.getTitle());
		/* 今日の天気を設定 */
		dto.setTodayWeather(response.getForecasts()
				.get(0)
				.getTelop());
		/* 明日の天気を設定 */
		dto.setTomorrowWeather(response.getForecasts()
				.get(1)
				.getTelop());
		/* 天気情報を返す */
		return dto;
	}
}