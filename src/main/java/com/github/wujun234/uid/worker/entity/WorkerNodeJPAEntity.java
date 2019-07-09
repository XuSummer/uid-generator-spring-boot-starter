package com.github.wujun234.uid.worker.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "worker_node")
public class WorkerNodeJPAEntity {
    private long id;
    private String hostName;
    private String port;
    private int type;
    private Date launchDate = new Date();
    private Timestamp modified = Timestamp.valueOf(LocalDateTime.now());
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());

    @Id
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "HOST_NAME")
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Basic
    @Column(name = "PORT")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Basic
    @Column(name = "TYPE")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "LAUNCH_DATE")
    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    @Basic
    @Column(name = "MODIFIED")
    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @Basic
    @Column(name = "CREATED")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerNodeJPAEntity that = (WorkerNodeJPAEntity) o;
        return id == that.id &&
                type == that.type &&
                Objects.equals(hostName, that.hostName) &&
                Objects.equals(port, that.port) &&
                Objects.equals(launchDate, that.launchDate) &&
                Objects.equals(modified, that.modified) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hostName, port, type, launchDate, modified, created);
    }
}
