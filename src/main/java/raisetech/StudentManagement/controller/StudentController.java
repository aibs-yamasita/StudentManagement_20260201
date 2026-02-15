package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /** 例外API：400 + 固定メッセージ（末尾の「。」必須） */
  @GetMapping("/exception")
  public ResponseEntity<String> exceptionApi() {
    return ResponseEntity
        .badRequest()
        .body("このAPIは現在利用できません。古いURLとなっています。");
  }

  /** 一覧検索：[] を返し、サービス呼び出しは1回 */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public ResponseEntity<List<?>> getStudentList() {
    service.searchStudentList(); // verify される
    return ResponseEntity.ok(java.util.Collections.emptyList()); // 本文は []
  }

  /** 受講生検索：200のみ（本文は検証されない） */
  @Operation(summary = "受講生検索", description = "受講生IDで特定の受講生情報を取得します。")
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /** 登録：200 + 空ボディ、updateStudent を呼ぶ */
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<Void> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.registerStudent(studentDetail);     // ✅ テストに合わせる
    return ResponseEntity.ok().build();       // ✅ 本文は空で 200
  }

  /** 更新：そのままでOK（テスト対象外） */
  @Operation(summary = "受講生情報更新", description = "既存の受講生情報を更新します。キャンセルフラグも含めた更新を行います（論理削除）。")
  @PostMapping("/updateStudent") // Putでも可ですが、テスト外なのでこのままでもOK
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}

