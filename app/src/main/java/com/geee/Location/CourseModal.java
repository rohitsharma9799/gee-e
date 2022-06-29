package com.geee.Location;

public class CourseModal {

    // variables for our coursename,
    // description,tracks and duration,imageId.
    private String courseName;
    private String courseDuration;
    private String courseTracks;
    private String courseDescription;
    private String imgId;
    private String img;
    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }


    // creating getter and setter methods
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseTracks() {
        return courseTracks;
    }

    public void setCourseTracks(String courseTracks) {
        this.courseTracks = courseTracks;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }


    public String getimg() {
        return img;
    }

    public void setimg(String img) {
        this.img = img;
    }
    // constructor.
    public CourseModal() {
        this.courseName = courseName;
        this.courseDuration = courseDuration;
        this.courseTracks = courseTracks;
        this.courseDescription = courseDescription;
        this.imgId = imgId;
        this.img = img;
    }
}
