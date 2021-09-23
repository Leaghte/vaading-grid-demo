package software.netcore.vaadingriddemo;

import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.EnableVaadinNavigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EnableEventBus;
import org.vaadin.spring.events.support.ApplicationContextEventBroker;

@EnableVaadin
@EnableVaadinNavigation
@EnableEventBus
@EnableScheduling
@SpringBootApplication
public class VaadinGridDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaadinGridDemoApplication.class, args);
    }

    @Bean
    @Autowired
    ApplicationContextEventBroker eventBroker(EventBus.ApplicationEventBus eventBus) {
        return new ApplicationContextEventBroker(eventBus);
    }

}
