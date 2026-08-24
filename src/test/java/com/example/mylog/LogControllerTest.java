package com.example.mylog;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.mylog.entity.Log;
import com.example.mylog.repository.LogRepository;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LogControllerTest {
	/* HTTPリクエストを模擬するためのMockMvc */
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private LogRepository logRepository;

	/* 処理確認用テストログのID */
	private Integer testLogId;

	@Test
	public void 一覧画面が正しく表示される() throws Exception {
		/* /logsへGETリクエストを送信 */
		mockMvc.perform(get("/logs"))
				/* HTTPステータスが200 OKであることを確認 */
				.andExpect(status().isOk())
				/* logs/index.htmlが表示されることを確認 */
				.andExpect(view().name("logs/index"));
	}

	@Test
	public void ログ登録画面が正しく表示される() throws Exception {
		/* /logs/newへGETリクエストを送信 */
		mockMvc.perform(get("/logs/new"))
				/* HTTPステータスが200 OKであることを確認 */
				.andExpect(status().isOk())
				/* logs/new.htmlが表示されることを確認 */
				.andExpect(view().name("logs/new"));
	}

	@Test
	public void ログ登録が正常に動作する() throws Exception {
		/* ログ登録処理へPOSTリクエストを送信 */
		mockMvc.perform(post("/logs/create")
				/* CSRF対策用のトークンを付ける */
				.with(csrf())
				.param("logDate", "2026-08-16")
				.param("genre", "テストジャンル")
				.param("content", "テスト内容"))

				/* 登録後にリダイレクトされることを確認 */
				.andExpect(status().is3xxRedirection())
				/* ログ一覧画面へリダイレクトされることを確認 */
				.andExpect(redirectedUrl("/logs"));
	}

	@Test
	public void ジャンル未入力の場合はバリデーションエラーになる() throws Exception {
		/* ジャンルを空欄にしてログ登録処理へPOSTリクエストを送信 */
		mockMvc.perform(post("/logs/create")
				/* CSRF対策用のトークンを付ける */
				.with(csrf())
				.param("logDate", "2026-08-16")
				.param("genre", "")
				.param("content", "テスト内容"))

				/* バリデーションエラー時は画面を再表示することを確認 */
				.andExpect(status().isOk())
				/* ログ登録画面が表示されることを確認 */
				.andExpect(view().name("logs/new"));
	}

	@Test
	public void 日付未入力の場合はバリデーションエラーになる() throws Exception {
		/* 日付を空欄にしてログ登録処理へPOSTリクエストを送信 */
		mockMvc.perform(post("/logs/create")
				/* CSRF対策用のトークンを付ける */
				.with(csrf())
				.param("logDate", "")
				.param("genre", "テストジャンル")
				.param("content", "テスト内容"))

				/* バリデーションエラー時は画面を再表示することを確認 */
				.andExpect(status().isOk())
				/* ログ登録画面が表示されることを確認 */
				.andExpect(view().name("logs/new"));
	}

	@Test
	public void 内容が500文字以上の場合バリデーションエラーになる() throws Exception {
		/* 501文字のテスト用文字列を作成 */
		String testContent = "あ".repeat(501);

		/* 501文字の内容を入力してログ登録処理へPOSTリクエストを送信 */
		mockMvc.perform(post("/logs/create")
				/* CSRF対策用のトークンを付ける */
				.with(csrf())
				.param("logDate", "2026-08-16")
				.param("genre", "テストジャンル")
				.param("content", testContent))

				/* バリデーションエラー時は画面を再表示することを確認 */
				.andExpect(status().isOk())
				/* ログ登録画面が表示されることを確認 */
				.andExpect(view().name("logs/new"));
	}

	@BeforeEach
	public void 処理確認用のテストデータ作成() {
		/* テスト用のログを作成 */
		Log testLog = new Log();

		testLog.setLogDate(LocalDate.of(2026, 8, 16));
		testLog.setGenre("テストジャンル");
		testLog.setContent("テスト内容");
		testLog.setCreatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
		testLog.setUpdatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));

		/* テストデータをデータベースへ保存 */
		Log savedLog = logRepository.save(testLog);

		/* 保存したログのIDを取得 */
		testLogId = savedLog.getId();
	}

	@Test
	public void 更新処理確認() throws Exception {
		/* ログ更新処理へPOSTリクエストを送信 */
		mockMvc.perform(post("/logs/" + testLogId + "/update")
				/* CSRF対策用のトークンを付ける */
				.with(csrf())
				.param("logDate", "2026-08-17")
				.param("genre", "更新後ジャンル")
				.param("content", "更新後内容"))

				/* 更新後にリダイレクトされることを確認 */
				.andExpect(status().is3xxRedirection())
				/* ログ一覧画面へリダイレクトされることを確認 */
				.andExpect(redirectedUrl("/logs"));

		/* 更新後のログをデータベースから取得 */
		Optional<Log> updateLog = logRepository.findById(testLogId);

		/* 日付が更新されていることを確認 */
		assertEquals(LocalDate.of(2026, 8, 17), updateLog.get().getLogDate());
		/* ジャンルが更新されていることを確認 */
		assertEquals("更新後ジャンル", updateLog.get().getGenre());
		/* 内容が更新されていることを確認 */
		assertEquals("更新後内容", updateLog.get().getContent());
		/* 登録日時は変更されていないことを確認 */
		assertEquals(LocalDateTime.of(2026, 8, 16, 10, 0), updateLog.get().getCreatedAt());
		/* 更新日時が変更されていることを確認 */
		assertNotEquals(LocalDateTime.of(2026, 8, 16, 10, 0), updateLog.get().getUpdatedAt());
	}

	@Test
	public void 削除処理確認() throws Exception {
		mockMvc.perform(post("/logs/" + testLogId + "/delete")
				.with(csrf()))

				/* 更新後にリダイレクトされることを確認 */
				.andExpect(status().is3xxRedirection())
				/* ログ一覧画面へリダイレクトされることを確認 */
				.andExpect(redirectedUrl("/logs"));

		/* 削除後のログをデータベースから取得 */
		Optional<Log> deleteLog = logRepository.findById(testLogId);

		/* ログが存在しないことを確認 */
		assertTrue(deleteLog.isEmpty());
	}

	@Test
	public void ジャンル検索確認() throws Exception {
		/* 検索対象となるテストログを作成 */
		Log log1 = new Log();
		log1.setLogDate(LocalDate.of(2026, 8, 16));
		log1.setGenre("予定");
		log1.setContent("病院へ行く");
		log1.setCreatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
		log1.setUpdatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
		Log log2 = new Log();
		log2.setLogDate(LocalDate.of(2026, 8, 17));
		log2.setGenre("学習");
		log2.setContent("Javaを勉強");
		log2.setCreatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
		log2.setUpdatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
		Log log3 = new Log();
		log3.setLogDate(LocalDate.of(2026, 8, 18));
		log3.setGenre("予定");
		log3.setContent("面接対策");
		log3.setCreatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
		log3.setUpdatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
		/* テストログをデータベースへ保存 */
		logRepository.save(log1);
		logRepository.save(log2);
		logRepository.save(log3);
		/* 「予定」のジャンルを指定してログ一覧を取得 */
		mockMvc.perform(get("/logs")
				.param("genre", "予定"))
				.andExpect(status().isOk())
				.andExpect(view().name("logs/index"));
	}

	@Test
	public void 検索結果が0件の場合() throws Exception {
		/* 存在しないジャンルを指定してログ一覧を取得 */
		mockMvc.perform(get("/logs")
				.param("genre", "存在しないジャンル"))
				.andExpect(status().isOk())
				.andExpect(view().name("logs/index"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void ページネーション確認() throws Exception {
		/* 11件のテストログを作成 */
		Log[] logs = new Log[11];
		for (int i = 0; i < 11; i++) {
			logs[i] = new Log();
			logs[i].setLogDate(LocalDate.of(2026, 8, 18));
			logs[i].setGenre("予定");
			logs[i].setContent("面接対策");
			logs[i].setCreatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
			logs[i].setUpdatedAt(LocalDateTime.of(2026, 8, 16, 10, 0));
			logRepository.save(logs[i]);
		}
		/* 2ページ目を指定してログ一覧を取得 */
		MvcResult result = mockMvc.perform(get("/logs")
				.param("page", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("logs/index"))
				/* 実行結果を取得 */
				.andReturn();
		/* Modelからページングされたログ情報を取得 */
		Page<Log> page = (Page<Log>) result.getModelAndView()
				.getModel()
				.get("logs");
		/* 全体で2ページになることを確認 */
		assertEquals(2, page.getTotalPages());
		/* 2ページ目には2件のログが表示されることを確認 */
		assertEquals(2, page.getContent().size());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void カレンダー画面が正常に表示される() throws Exception {
		/* カレンダー画面へGETリクエストを送信 */
		MvcResult result = mockMvc.perform(get("/logs/calendar"))
				.andExpect(status().isOk())
				.andExpect(view().name("logs/calendar"))
				.andReturn();
		/* Modelからログ一覧を取得 */
		List<Log> logs = (List<Log>) result.getModelAndView()
				.getModel()
				.get("logs");
		/* ログが1件取得されることを確認 */
		assertEquals(1, logs.size());
		/* ログの日付が正しいことを確認 */
		assertEquals(LocalDate.of(2026, 8, 16),logs.get(0).getLogDate());
		/* ログのジャンルが正しいことを確認 */
		assertEquals("テストジャンル", logs.get(0).getGenre());
		/* ログの内容が正しいことを確認 */
		assertEquals("テスト内容", logs.get(0).getContent());
	}

	@Test
	public void 編集画面が正常に表示される() throws Exception {
		/* 指定したログの編集画面へGETリクエストを送信 */
		MvcResult result = mockMvc.perform(
				get("/logs/" + testLogId + "/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("logs/edit"))
				.andReturn();
		/* Modelから編集対象のログを取得 */
		Log log = (Log) result.getModelAndView()
				.getModel()
				.get("log");
		/* 編集対象のログIDが正しいことを確認 */
		assertEquals(testLogId, log.getId());
	}

	@Test
	public void 更新時に日付未入力の場合はバリデーションエラーになる() throws Exception {
		/* 日付を未入力にしてログ更新処理へPOSTリクエストを送信 */
		mockMvc.perform(post("/logs/" + testLogId + "/update")
				.with(csrf())
				.param("logDate", "")
				.param("genre", "更新後ジャンル")
				.param("content", "更新後内容"))
				.andExpect(status().isOk())
				.andExpect(view().name("logs/edit"));
	}

	@Test
	public void カレンダーから編集した場合はカレンダーへ戻る() throws Exception {
		/* カレンダーからログ更新処理へPOSTリクエストを送信 */
		mockMvc.perform(post("/logs/" + testLogId + "/update")
				.with(csrf())
				.param("logDate", "2026-08-17")
				.param("genre", "更新後ジャンル")
				.param("content", "更新後内容")
				.param("from", "calendar"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/logs/calendar"));
	}
}
