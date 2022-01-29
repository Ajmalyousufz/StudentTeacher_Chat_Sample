package com.ajmalyousufza.teacherstudentchat.ModelClass;

public class Users {

    String teacher_id;
    String teacher_name;
    String teacher_password;
    String teacher_prof_img_uri;
    String userType;

    public Users() {
    }

    public Users(String teacher_id, String teacher_name, String teacher_password, String teacher_prof_img_uri, String userType) {
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
        this.teacher_password = teacher_password;
        this.teacher_prof_img_uri = teacher_prof_img_uri;
        this.userType = userType;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_password() {
        return teacher_password;
    }

    public void setTeacher_password(String teacher_password) {
        this.teacher_password = teacher_password;
    }

    public String getTeacher_prof_img_uri() {
        return teacher_prof_img_uri;
    }

    public void setTeacher_prof_img_uri(String teacher_prof_img_uri) {
        this.teacher_prof_img_uri = teacher_prof_img_uri;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
