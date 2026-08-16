package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.CourseApplicationStatus;

@Mapper
public interface CourseApplicationStatusRepository {

  @Select("""
      SELECT id,
             student_course_id AS studentCourseId,
             status
        FROM course_application_status
      """)
  List<CourseApplicationStatus> findAll();

  @Select("""
      SELECT id,
             student_course_id AS studentCourseId,
             status
        FROM course_application_status
       WHERE student_course_id = #{studentCourseId}
      """)
  CourseApplicationStatus findByStudentCourseId(int studentCourseId);
}