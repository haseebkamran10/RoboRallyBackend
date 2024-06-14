package dk.dtu.compute.se.pisd.roborally;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RoboRallyApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RoboRallyApplication.class);
		ConfigurableApplicationContext context = app.run(args);

		System.out.println("Application has started");

		// Keep the context open
		synchronized (context) {
			try {
				context.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Bean
	public ApplicationLifecycleListener applicationLifecycleListener() {
		return new ApplicationLifecycleListener();
	}

	public static class ApplicationLifecycleListener {
		@EventListener
		public void onContextRefreshed(ContextRefreshedEvent event) {
			System.out.println("Context refreshed: " + event);
		}

		@EventListener
		public void onContextClosed(ContextClosedEvent event) {
			System.out.println("Context closed: " + event);
		}
	}
}
