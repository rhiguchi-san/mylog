package com.example.mylog.dto;

import lombok.Data;

/* =========================
   天気予報DTO
   各日の天気予報情報を保持
   ========================= */
@Data
public class Forecast {

	/* 日付ラベル（今日・明日など） */
	private String dateLabel;

	/* 天気情報 */
	private String telop;
}
