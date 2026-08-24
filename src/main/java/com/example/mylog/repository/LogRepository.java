package com.example.mylog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mylog.entity.Log;

/* =========================
   ログ情報リポジトリ
   ログ情報のデータベース操作を管理
   ========================= */
public interface LogRepository extends JpaRepository<Log, Integer> {

	/* ログを日付の昇順で全件取得する */
	List<Log> findAllByOrderByLogDateAsc();

	/* ログを日付の昇順でページング取得する */
	Page<Log> findAllByOrderByLogDateAsc(Pageable pageable);

	/* 指定したジャンルのログを日付の昇順でページング取得する */
	Page<Log> findByGenreOrderByLogDateAsc(String genre, Pageable pageable);
}