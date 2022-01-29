package com.ajmalyousufza.teacherstudentchat.ModelClass;

public class SrudentUserModel {

        String student_id;
        String student_name;
        String student_password;
        String student_prof_img_uri;
        String teacherId;
        String userType;

        public SrudentUserModel() {
        }

        public SrudentUserModel(String student_id, String student_name, String student_password, String student_prof_img_uri, String teacherId, String userType) {
                this.student_id = student_id;
                this.student_name = student_name;
                this.student_password = student_password;
                this.student_prof_img_uri = student_prof_img_uri;
                this.teacherId = teacherId;
                this.userType = userType;
        }

        public String getStudent_id() {
                return student_id;
        }

        public void setStudent_id(String student_id) {
                this.student_id = student_id;
        }

        public String getStudent_name() {
                return student_name;
        }

        public void setStudent_name(String student_name) {
                this.student_name = student_name;
        }

        public String getStudent_password() {
                return student_password;
        }

        public void setStudent_password(String student_password) {
                this.student_password = student_password;
        }

        public String getStudent_prof_img_uri() {
                return student_prof_img_uri;
        }

        public void setStudent_prof_img_uri(String student_prof_img_uri) {
                this.student_prof_img_uri = student_prof_img_uri;
        }

        public String getTeacherId() {
                return teacherId;
        }

        public void setTeacherId(String teacherId) {
                this.teacherId = teacherId;
        }

        public String getUserType() {
                return userType;
        }

        public void setUserType(String userType) {
                this.userType = userType;
        }
}

