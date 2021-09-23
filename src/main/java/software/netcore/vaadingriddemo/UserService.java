package software.netcore.vaadingriddemo;

import com.github.javafaker.Faker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService implements InitializingBean {

    private static final int USERS_LIMIT = 1000;
    private static final Faker FAKER = new Faker();

    private final AtomicBoolean running = new AtomicBoolean(false);

    @NonNull
    private final ApplicationEventPublisher eventPublisher;
    @NonNull
    private final UserRepository repository;

    @Override
    public void afterPropertiesSet() {
        IntStream.rangeClosed(1, USERS_LIMIT)
                .forEach(value -> repository
                        .save(User.builder()
                                .firstname(FAKER.name().firstName())
                                .lastname(FAKER.name().lastName())
                                .username(FAKER.name().username())
                                .online(FAKER.bool().bool())
                                .build()));
    }

    public Page<User> listUsers(@NonNull Pageable pageable) {
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
