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
    private String userId;
    private String fileName;
    private Integer fileSize;
    private String fileType;
    private String fileData;
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
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    @Column(name = "file_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    @Basic
    @Column(name = "upload_time")
    public Timestamp getCreateTime() {
        return uploadTime;
    }

    public void setCreateTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysFileEntity that = (SysFileEntity) o;
        return id == that.id &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(fileSize, that.fileSize) &&
                Objects.equals(fileType, that.fileType) &&
                Objects.equals(fileData, that.fileData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, fileName, fileSize, fileType, fileData);
    }
}
