package com.github.wujun234.uid.worker.dao;

import com.github.wujun234.uid.worker.entity.WorkerNodeJPAEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WorkerNodeJPADAO extends JpaRepository<WorkerNodeJPAEntity, Long> {
}
