package com.example.mylog.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
	@NotNull(message = "日付を入力してください")
	private LocalDate logDate;

	/* 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/* 更新日時*/
	@Column(name = "update_at")
	private LocalDateTime updatedAt;

	/* 種類 */
	@Column(name = "genre")
	@NotBlank(message = "ジャンルを選択してください")
	private String genre;

	/* 内容 */
	@Column(name = "content")
	@NotBlank(message = "内容を入力してください")
	@Size(max = 500, message = "内容は500文字以内で入力してください")
	private String content;

}
