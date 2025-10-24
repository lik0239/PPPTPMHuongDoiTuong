package com.example.courseReg.repo;

public interface CourseClassView {
    Long getClassId();
    String getCode();
    String getName();
    Integer getCredits();
    String getGroupCode();
    String getTeacher();
    String getSchedule();
    Integer getCapacity();
    Integer getEnrolled();
}
