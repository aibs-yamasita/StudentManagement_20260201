package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索が実行できて空で返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  // private void andExcept(ResultMatcher ok) {
  //}

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    //リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    //本来であれば帰りは登録されたデータが入るが、モックかすると意味がないため、レスポンスは作らない。
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "student":{
                        "name":"江並康介",
                        "kanaName":"エナミ",
                        "nickname":"コウジ",
                        "email":"test@example.com",
                        "area":"奈良",
                        "age":36,
                        "sex":"男性",
                        "remark":""
                    },
                    "studentCourseList":[
                    {
                          "courseName":"Javaコース"
                    }
                    ]
                }
                """))
        .andDo(print())
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setSex("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setSex("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }


  @Test
  @DisplayName("受講生詳細の一覧検索テスト")
  void 受講生詳細の登録が実行できて空でかえってくること()
      throws Exception {
    //リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "student":{
                    "id" : "12",
                    "name" : "江並康介",
                    "kanaName" : "エナミ",
                    "nickname" : "コウジ",
                    "email" : "test@example.com",
                    "area" : "奈良",
                    "age"  : 36,
                    "sex" : "男性",
                    "remark" : ""
                  },
                  "studentCourseList" : [
                    {
                      "id": "1",
                      "studentId" : "1",
                      "courseName" : "Javaコース",
                      "courseStartAt": "2024-04-27T10:50:39.833614",
                      "courseEndAt": "2025-04-27T10:50:39.833614"
                    }
                  ]
                }
                """)
        )
        .andDo(print())
        .andExpect(status().isOk());
        //.andDo(print())
        //.andExpect(status().is4xxClientError());



    verify(service, times(1)).registerStudent(any());
  }


  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    String expectedContent;
    mockMvc.perform(get("/exception"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));

  }
}
