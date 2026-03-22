package raisetech.StudentManagement.Repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;


  @Test
  void 受講生の全件検索が行えること(){
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setAge(36);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }


   @Test
  void 受講生コース情報を全件取得できること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual).isNotEmpty();
  }


 @Test
  void IDを指定して受講生を検索できること() {
    Student actual = sut.searchStudent("1");

    // nullでないこと
    assertThat(actual).isNotNull();

    // 内容が正しいこと（data.sql に基づく）
    assertThat(actual.getId()).isEqualTo("1");
    assertThat(actual.getName()).isEqualTo("山田太郎");
    assertThat(actual.getKanaName()).isEqualTo("ヤマダタロウ");
    assertThat(actual.getEmail()).isEqualTo("taro@example.com");
    assertThat(actual.isDeleted()).isFalse();
  }


  @Test
  void 受講生IDを指定してコース情報を取得できること() {
    List<StudentCourse> actual = sut.searchStudentCourse("1");

    // 少なくとも1件はある
    assertThat(actual).isNotEmpty();

    // student_id = 1 のデータだけであること
    assertThat(actual)
        .allMatch(course -> course.getStudentId().equals("1"));

    // 期待するコース名が含まれていること
    assertThat(actual)
        .extracting(StudentCourse::getCourseName)
        .contains("Javaコース");
  }


  @Test
  void 受講生コース情報を登録できること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId("1");
    course.setCourseName("テストコース");
    course.setCourseStartAt(LocalDateTime.now());
    course.setCourseEndAt(LocalDateTime.now().plusMonths(1));

    sut.registerStudentCourse(course);

    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertThat(actual)
        .extracting(StudentCourse::getCourseName)
        .contains("テストコース");
  }



}