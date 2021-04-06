package com.xiaosong.myframework.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Description
 * @Author msli
 * @Date 2021/01/26
 */

@Entity
@Table(name = "t_sys_file", schema = "xiaosong", catalog = "")

public class SysFileEntity implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    private int id;
    private String uid;
    private String fileName;
    private String fileType;
    private Integer fileSize;
    private String fileContent;
    private String recognitionContent;
    private String sourceGroup;
    private Boolean star = false;
    private Timestamp uploadTime;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "uid")
    public String getUid() {
        return uid;
    }

    public void setUid(String userId) {
        this.uid = userId;
    }

    @Basic
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "file_size")
    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    @Basic
    @Column(name = "file_type")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Basic
    @Column(name = "file_content")
    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileData) {
        this.fileContent = fileData;
    }

    @Basic
    @Column(name = "recognition_content")
    public String getRecognitionContent() {
        return recognitionContent;
    }

    public void setRecognitionContent(String recognitionContent) {
        this.recognitionContent = recognitionContent;
    }

    @Basic
    @Column(name = "source_group")
    public String getSourceGroup() {
        return sourceGroup;
    }

    public void setSourceGroup(String sourceGroup) {
        this.sourceGroup = sourceGroup;
    }

    @Basic
    @Column(name = "upload_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Basic
    @Column(name = "star")
    public Boolean getStar() {
        return star;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysFileEntity that = (SysFileEntity) o;
        return id == that.id &&
                Objects.equals(uid, that.uid) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(fileSize, that.fileSize) &&
                Objects.equals(fileType, that.fileType) &&
                Objects.equals(fileContent, that.fileContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, fileName, fileSize, fileType, fileContent);
    }
}
