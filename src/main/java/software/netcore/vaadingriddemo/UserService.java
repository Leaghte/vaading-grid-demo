package software.netcore.vaadingriddemo;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService implements InitializingBean {

    private static final int USERS_LIMIT = 1000;
    private static final Faker FAKER = new Faker();

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository repository;

    public UserService(ApplicationEventPublisher eventPublisher,
                       UserRepository repository) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
    }

    @Override
    public void afterPropertiesSet() {
        IntStream.rangeClosed(1, USERS_LIMIT)
                .forEach(value -> {
                    User user = new User();
                    user.setFirstname(FAKER.name().firstName());
                    user.setLastname(FAKER.name().lastName());
                    user.setUsername(FAKER.name().username());
                    user.setOnline(FAKER.bool().bool());
                    repository.save(user);
                });
    }

    public Page<User> listUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int countUsers() {
        return Math.toIntExact(repository.count());
    }

    public void run() {
        running.set(true);
    }

    public void stop() {
        running.set(false);
    }

    public boolean isRunning() {
        return running.get();
    }

    @Scheduled(fixedRate = 1000)
    void doChanges() {
        if (running.get()) {
            List<User> users = repository.findAll()
                    .stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            collected -> {
                                Collections.shuffle(collected);
                                return collected.stream();
                            }))
                    .limit(100)
                    .peek(user -> {
                        user.setUsername(FAKER.name().username());
                        user.setOnline(FAKER.bool().bool());
                    })
                    .collect(Collectors.toList());

            repository.saveAll(users);

            users.stream()
                    .map(UserChangedEvent::new)
                    .forEach(eventPublisher::publishEvent);
        }
    }

}
