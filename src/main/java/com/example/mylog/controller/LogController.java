package com.example.mylog.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mylog.entity.Log;
import com.example.mylog.service.LogService;

/* =========================
   ログ情報コントローラ
   ログの登録・一覧表示を管理
   http://localhost:8080/logs
   ========================= */
@Controller
@RequestMapping("/logs")
public class LogController {
	/* ログ情報サービス */
	private final LogService logService;

	/* コンストラクタインジェクション */
	public LogController(LogService logService) {
		this.logService = logService;
	}

	/* ログ一覧画面を表示 */
	@GetMapping
	public String index(
			@RequestParam(defaultValue = "") String genre,
			Model model) {
		/* ログ一覧をViewへ渡す */
		model.addAttribute("logs", logService.findByGenre(genre));
		/* 検索条件をViewへ渡す */
		model.addAttribute("genre", genre);
		/* ログ一覧画面を表示 */
		return "logs/index";
	}

	/* カレンダー画面を表示 */
	@GetMapping("/calendar")
	public String calendar(Model model) {
		/* ログ一覧をViewへ渡す */
		model.addAttribute("logs", logService.findAll());
		/* カレンダー画面を表示 */
		return "logs/calendar";
	}

	/* ログ登録画面を表示 */
	@GetMapping("/new")
	public String showNewForm(Model model) {
		/* 空のログオブジェクトをViewへ渡す */
		model.addAttribute("log", new Log());
		/* ログ登録画面を表示 */
		return "logs/new";
	}

	/* ログ編集画面を表示 */
	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Integer id,
			Model model,
			/* 遷移元（一覧画面またはカレンダー画面）を取得 */
			@RequestParam(required = false) String from) {
		/* IDから編集対象のログを取得 */
		Log log = logService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("存在しないIDです"));
		/* 編集対象のログ情報をViewへ渡す */
		model.addAttribute("log", log);
		/* 遷移元情報をViewへ渡す */
		model.addAttribute("from", from);
		/* ログ編集画面を表示 */
		return "logs/edit";
	}

	/* ログを登録する */
	@PostMapping("/create")
	public String create(
			/* 入力されたログ情報を取得 */
			@Valid @ModelAttribute Log log,
			/* バリデーション結果を取得 */
			BindingResult result,
			/* 遷移元を取得 */
			@RequestParam(required = false) String from) {
		if (result.hasErrors()) {
			return "logs/new";
		}
		/* ログを保存 */
		logService.save(log);
		/* カレンダー画面から登録した場合はカレンダーへ戻る */
		if ("calendar".equals(from)) {
			return "redirect:/logs/calendar";
		}
		/* ログ一覧画面へリダイレクト */
		return "redirect:/logs";
	}

	/* ログを更新する */
	@PostMapping("/{id}/update")
	public String update(@PathVariable Integer id,
			@Valid @ModelAttribute Log log,
			/* バリデーション結果を取得 */
			BindingResult result,
			/* 遷移元を取得 */
			@RequestParam(required = false) String from) {
		/* URLから取得したIDを設定 */
		log.setId(id);
		
		if (result.hasErrors()) {
			return "logs/edit";
		}
		/* ログを保存 */
		logService.update(log);
		/* カレンダー画面から編集した場合はカレンダーへ戻る */
		if ("calendar".equals(from)) {
			return "redirect:/logs/calendar";
		}
		/* ログ一覧画面へリダイレクト */
		return "redirect:/logs";
	}

	/* ログを削除する */
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Integer id) {
		/* 指定されたIDのログを削除 */
		logService.delete(id);
		/* ログ一覧画面へリダイレクト */
		return "redirect:/logs";
	}

}
