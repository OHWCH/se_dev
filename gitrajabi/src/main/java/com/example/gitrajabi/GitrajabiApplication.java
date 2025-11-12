package com.example.gitrajabi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration; // 나중에 지움

/*
 * [수정] @SpringBootApplication에 exclude 속성을 추가합니다.
 *
 * [주석] 코드의 목적 (변경 탄력성):
 * 현재 GitHub API 기능(27, 28, 29)을 병렬로 개발 중이며,
 * 아직 데이터베이스 연결(JPA) 기능은 필요하지 않습니다.
 * 이 'exclude' 구문은 Spring Boot가 데이터베이스 설정을
 * 자동으로 시도하는 것을 '임시로 중지'시킵니다.
 * (올바른 기능 수행 원칙 준수 - API 기능 테스트에 집중)
 *
 * [미래]
 * 나중에 팀원이 데이터베이스 연동을 시작할 때,
 * 1. 이 'exclude' 구문을 제거하고,
 * 2. application.properties에 spring.datasource.url 등을 추가하면
 * 정상적으로 JPA 기능이 활성화됩니다.
 */
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
public class GitrajabiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitrajabiApplication.class, args);
	}

}
