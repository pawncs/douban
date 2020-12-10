package fm.douban;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DoubanApplication {

	public static void main(String[] args) {
		Logger log = Logger.getLogger(DoubanApplication.class);
		SpringApplication.run(DoubanApplication.class, args);
		log.info("启动成功");
	}

}
