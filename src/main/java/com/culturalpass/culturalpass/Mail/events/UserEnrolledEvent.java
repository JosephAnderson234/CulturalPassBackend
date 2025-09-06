package com.culturalpass.culturalpass.Mail.events;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.User.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserEnrolledEvent extends ApplicationEvent {
    private final User user;
    private final Event event;
    private final String token;

    public UserEnrolledEvent(Object source, User user, Event event, String token) {
        super(source);
        this.user = user;
        this.event = event;
        this.token = token;
    }
}