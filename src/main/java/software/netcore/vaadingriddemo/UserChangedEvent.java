package software.netcore.vaadingriddemo;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserChangedEvent extends ApplicationEvent {

    private final User user;

    public UserChangedEvent(@NonNull User user) {
        super(Thread.currentThread());
        this.user = user;
    }

}
