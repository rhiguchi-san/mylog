package com.example.mylog.dto;

import java.util.List;

import lombok.Data;

/* =========================
   天気予報レスポンスDTO
   天気予報APIのレスポンスを保持
   ========================= */
@Data
public class WeatherResponse {

	/* 天気予報のタイトル */
	private String title;

	/* 天気予報一覧 */
	private List<Forecast> forecasts;
}