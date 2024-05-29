package memoraize.global.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 비동기 처리 스레드 풀 설정
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean(name = "voiceTaskExecutor")
	public Executor voiceTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);     // 기본 스레드 수
		executor.setMaxPoolSize(3);     // 최대 스레드 수
		executor.setQueueCapacity(150);  // 작업 큐 용량
		executor.setThreadNamePrefix("voice-"); // 스레드 이름 접두사
		executor.setKeepAliveSeconds(30); // 최대 유휴 시간 (초 단위)
		executor.setAllowCoreThreadTimeOut(true); // 코어 스레드 타임아웃 허용
		executor.initialize();
		return executor;
	}

	@Bean(name = "visionThreadPool")
	public ExecutorService visionThreadPool() {
		return createVisionThreadPool();
	}

	public static ExecutorService createVisionThreadPool() {
		int corePoolSize = 3;
		int maxPoolSize = 5;
		int queueCapacity = 100;

		ThreadPoolExecutor executor = new ThreadPoolExecutor(
			corePoolSize,
			maxPoolSize,
			60L,
			TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(queueCapacity),
			new ThreadPoolExecutor.CallerRunsPolicy()
		);

		return executor;
	}
}
