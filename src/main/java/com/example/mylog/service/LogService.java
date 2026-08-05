package com.example.mylog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.mylog.entity.Log;
import com.example.mylog.repository.LogRepository;

/* =========================
   ログ情報サービス
   ログの登録・取得・更新・削除処理を管理
   ========================= */
@Service
public class LogService {

	/* ログ情報リポジトリ */
	private final LogRepository logRepository;

	/* コンストラクタインジェクション */
	public LogService(LogRepository logRepository) {
		this.logRepository = logRepository;
	}

	/* すべてのログを取得する */
	public List<Log> findAll() {
		/* ログ一覧を取得 */
		return logRepository.findAllByOrderByLogDateAsc();
	}

	/* ジャンルでログを検索する */
	public List<Log> findByGenre(String genre) {
		/* ジャンルが未指定の場合 */
		if (genre == null || genre.isBlank()) {
			return logRepository.findAllByOrderByLogDateAsc();
		}
		/* 指定したジャンルのログを取得 */
		return logRepository.findByGenreOrderByLogDateAsc(genre);
	}
	
	/* 指定したIDのログを取得する */
	public Optional<Log> findById(Integer id) {
		/* 指定したIDのログを取得 */
		return logRepository.findById(id);
	}

	/* ログを登録する */
	public void save(Log log) {
		/* 現在日時を取得*/
		LocalDateTime now = LocalDateTime.now();
		/* 登録日時を設定 */
		log.setCreatedAt(now);
		/* 更新日時を設定 */
		log.setUpdatedAt(now);
		/* ログをデータベースへ保存 */
		logRepository.save(log);
	}

	/* ログを更新する */
	public void update(Log log) {
		/* 更新内容をデータベースへ保存 */
		log.setUpdatedAt(LocalDateTime.now());
		logRepository.save(log);
	}

	/* ログを削除する */
	public void delete(Integer id) {
		/* 指定したIDのログを削除 */
		logRepository.deleteById(id);
	}

}