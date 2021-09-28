package software.netcore.vaadingriddemo;

import org.springframework.context.ApplicationEvent;

public class UserChangedEvent extends ApplicationEvent {

    private final User user;

    public UserChangedEvent(User user) {
        super(Thread.currentThread());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
