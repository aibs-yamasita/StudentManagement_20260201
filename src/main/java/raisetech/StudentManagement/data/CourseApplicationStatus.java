package raisetech.StudentManagement.data;

public class CourseApplicationStatus {

  private Integer id;
  private Integer studentCourseId;
  private ApplicationStatus status;

  // id
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  // student_course_id
  public Integer getStudentCourseId() {
    return studentCourseId;
  }

  public void setStudentCourseId(Integer studentCourseId) {
    this.studentCourseId = studentCourseId;
  }

  // status
  public ApplicationStatus getStatus() {
    return status;
  }

  public void setStatus(ApplicationStatus status) {
    this.status = status;
  }
}