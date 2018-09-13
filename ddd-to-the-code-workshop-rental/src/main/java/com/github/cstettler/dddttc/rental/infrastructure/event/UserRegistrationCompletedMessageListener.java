package com.github.cstettler.dddttc.rental.infrastructure.event;

import com.github.cstettler.dddttc.rental.application.UserService;
import com.github.cstettler.dddttc.rental.domain.UserAlreadyExistsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.github.cstettler.dddttc.rental.domain.FirstName.firstName;
import static com.github.cstettler.dddttc.rental.domain.LastName.lastName;
import static com.github.cstettler.dddttc.rental.domain.UserId.userId;

@Component
class UserRegistrationCompletedMessageListener {

    private final UserService userService;

    UserRegistrationCompletedMessageListener(UserService userService) {
        this.userService = userService;
    }

    @JmsListener(destination = "registration/user-registration-completed")
    public void onUserRegistrationCompleted(UserRegistrationCompletedMessage message) {
        try {
            String userHandle = message.userHandle.value;
            String firstName = message.fullName.firstName;
            String lastName = message.fullName.lastName;

            this.userService.addUser(userId(userHandle), firstName(firstName), lastName(lastName));
        } catch (UserAlreadyExistsException e) {
            // ignored
        }
    }


    public static class UserRegistrationCompletedMessage {

        ValueWrapper userHandle;
        FullName fullName;

    }


    public static class FullName {

        String firstName;
        String lastName;

    }


    public static class ValueWrapper {

        String value;

    }

}
