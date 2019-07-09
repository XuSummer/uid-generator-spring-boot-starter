/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wujun234.uid.worker;

import com.github.wujun234.uid.worker.dao.WorkerNodeMybatisDAO;
import com.github.wujun234.uid.worker.entity.WorkerNodeMybatisEntity;
import com.github.wujun234.uid.utils.DockerUtils;
import com.github.wujun234.uid.utils.NetUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Represents an implementation of {@link WorkerIdAssigner},
 * the worker id will be discarded after assigned to the UidGenerator
 *
 * @author yutianbao
 */
public class DisposableMybatisWorkerIdAssigner implements WorkerIdAssigner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisposableMybatisWorkerIdAssigner.class);

    @Resource
    private WorkerNodeMybatisDAO workerNodeMybatisDAO;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return assigned worker id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long assignWorkerId() {
        // build worker node entity
        WorkerNodeMybatisEntity workerNodeMybatisEntity = buildWorkerNode();

        // add worker node for new (ignore the same IP + PORT)
        workerNodeMybatisDAO.addWorkerNode(workerNodeMybatisEntity);
        LOGGER.info("Add worker node:" + workerNodeMybatisEntity);

        return workerNodeMybatisEntity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long assignFakeWorkerId() {
        return buildFakeWorkerNode().getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkerNodeMybatisEntity buildWorkerNode() {
        WorkerNodeMybatisEntity workerNodeMybatisEntity = new WorkerNodeMybatisEntity();
        if (DockerUtils.isDocker()) {
            workerNodeMybatisEntity.setType(WorkerNodeType.CONTAINER.value());
            workerNodeMybatisEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeMybatisEntity.setPort(DockerUtils.getDockerPort());
        } else {
            workerNodeMybatisEntity.setType(WorkerNodeType.ACTUAL.value());
            workerNodeMybatisEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeMybatisEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
        }

        return workerNodeMybatisEntity;
    }

    private WorkerNodeMybatisEntity buildFakeWorkerNode() {
        WorkerNodeMybatisEntity workerNodeMybatisEntity = new WorkerNodeMybatisEntity();
        workerNodeMybatisEntity.setType(WorkerNodeType.FAKE.value());
        if (DockerUtils.isDocker()) {
            workerNodeMybatisEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeMybatisEntity.setPort(DockerUtils.getDockerPort() + "-" + RandomUtils.nextInt(100000));
        }else {
            workerNodeMybatisEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeMybatisEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
        }
        return workerNodeMybatisEntity;
    }
}
