package com.example.mylog.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/* =========================
   ログエンティティ
   システムログ情報を管理
   ========================= */
@Entity
@Table(name = "logs")
@Data
public class Log {

	/* ID（主キー） */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/* 日付*/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "log_date")
	private LocalDate logDate;

	/* 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/* 更新日時*/
	@Column(name = "update_at")
	private LocalDateTime updatedAt;

	/* 種類 */
	@Column(name = "genre")
	private String genre;

	/* 内容 */
	@Column(name = "content")
	private String content;

}
