package com.example.mylog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mylog.entity.Log;

/* =========================
   ログ情報リポジトリ
   ログテーブルへのデータベース操作を管理
   ========================= */
public interface LogRepository extends JpaRepository<Log, Integer> {
	/* ログを日付の昇順で全件取得する */
	List<Log> findAllByOrderByLogDateAsc();

	List<Log> findByGenreOrderByLogDateAsc(String genre);
}
