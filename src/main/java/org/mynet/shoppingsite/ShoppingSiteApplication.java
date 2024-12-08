package org.mynet.shoppingsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.mynet.shoppingsite")
@EnableJpaRepositories("org.mynet.shoppingsite.repository")  // 指定仓库包路径
@EntityScan("org.mynet.shoppingsite.model")  // 指定实体类包路径
// 明确指定扫描路径
public class ShoppingSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingSiteApplication.class, args);
	}

}
