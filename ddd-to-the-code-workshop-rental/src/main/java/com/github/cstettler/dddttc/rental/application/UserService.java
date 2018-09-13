package com.github.cstettler.dddttc.rental.application;

import com.github.cstettler.dddttc.rental.domain.FirstName;
import com.github.cstettler.dddttc.rental.domain.LastName;
import com.github.cstettler.dddttc.rental.domain.User;
import com.github.cstettler.dddttc.rental.domain.UserAlreadyExistsException;
import com.github.cstettler.dddttc.rental.domain.UserId;
import com.github.cstettler.dddttc.rental.domain.UserRepository;
import com.github.cstettler.dddttc.stereotype.ApplicationService;

import static com.github.cstettler.dddttc.rental.domain.User.newUser;

@ApplicationService
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(UserId userId, FirstName firstName, LastName lastName) throws UserAlreadyExistsException {
        User user = newUser(userId, firstName, lastName);

        this.userRepository.add(user);
    }

}
