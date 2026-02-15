package raisetech.StudentManagement.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private final StudentConverter converter = new StudentConverter();

  @Test
  void 受講生とコースが正しく紐づくこと() {

    // ① 学生を作る
    Student student = new Student();
    student.setId("1");
    student.setName("山田");

    // ② コースを作る（studentIdを一致させる）
    StudentCourse course = new StudentCourse();
    course.setId("10");
    course.setStudentId("1");
    course.setCourseName("Javaコース");

    // ③ convertを実行
    List<StudentDetail> result =
        converter.convertStudentDetails(
            List.of(student),
            List.of(course)
        );

    // ④ 結果を確認
    assertEquals(1, result.size());
    assertEquals("1", result.get(0).getStudent().getId());
    assertEquals(1, result.get(0).getStudentCourseList().size());
    assertEquals("Javaコース",
        result.get(0).getStudentCourseList().get(0).getCourseName());
  }
}


