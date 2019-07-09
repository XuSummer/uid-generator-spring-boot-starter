package com.github.wujun234.uid;

import com.github.wujun234.uid.impl.CachedUidGenerator;
import com.github.wujun234.uid.impl.DefaultUidGenerator;
import com.github.wujun234.uid.impl.UidProperties;
import com.github.wujun234.uid.worker.DisposableJPAWorkerIdAssigner;
import com.github.wujun234.uid.worker.DisposableMybatisWorkerIdAssigner;
import com.github.wujun234.uid.worker.WorkerIdAssigner;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;

/**
 * UID 的自动配置
 *
 * @author wujun
 * @date 2019.02.20 10:57
 */
@Configuration
@ConditionalOnClass({ DefaultUidGenerator.class, CachedUidGenerator.class })
@MapperScan({ "com.github.wujun234.uid.worker.dao" })
@EnableConfigurationProperties(UidProperties.class)
public class UidAutoConfigure {

	@Autowired
	private UidProperties uidProperties;

	@Bean
	@ConditionalOnMissingBean
	@Lazy
	DefaultUidGenerator defaultUidGenerator() {
		return new DefaultUidGenerator(uidProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy
	CachedUidGenerator cachedUidGenerator() {
		return new CachedUidGenerator(uidProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    WorkerIdAssigner workerIdAssigner1() {
		return new DisposableMybatisWorkerIdAssigner();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(JpaRepository.class)
    WorkerIdAssigner workerIdAssigner2() {
		return new DisposableJPAWorkerIdAssigner();
	}
}
