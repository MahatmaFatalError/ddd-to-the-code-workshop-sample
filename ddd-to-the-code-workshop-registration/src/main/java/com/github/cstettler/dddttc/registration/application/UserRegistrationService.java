package com.github.cstettler.dddttc.registration.application;

import com.github.cstettler.dddttc.registration.domain.FullName;
import com.github.cstettler.dddttc.registration.domain.PhoneNumber;
import com.github.cstettler.dddttc.registration.domain.PhoneNumberNotYetVerifiedException;
import com.github.cstettler.dddttc.registration.domain.PhoneNumberVerificationCodeInvalidException;
import com.github.cstettler.dddttc.registration.domain.UserHandle;
import com.github.cstettler.dddttc.registration.domain.UserHandleAlreadyInUseException;
import com.github.cstettler.dddttc.registration.domain.UserRegistration;
import com.github.cstettler.dddttc.registration.domain.UserRegistration.UserRegistrationFactory;
import com.github.cstettler.dddttc.registration.domain.UserRegistrationAlreadyCompletedException;
import com.github.cstettler.dddttc.registration.domain.UserRegistrationId;
import com.github.cstettler.dddttc.registration.domain.UserRegistrationNotExistingException;
import com.github.cstettler.dddttc.registration.domain.UserRegistrationRepository;
import com.github.cstettler.dddttc.registration.domain.VerificationCode;
import com.github.cstettler.dddttc.stereotype.ApplicationService;
import com.github.cstettler.dddttc.support.domain.DomainEventPublisher;

import static com.github.cstettler.dddttc.registration.domain.UserRegistrationCompletedEvent.userRegistrationCompleted;

@ApplicationService
public class UserRegistrationService {

    private final UserRegistrationFactory userRegistrationFactory;
    private final UserRegistrationRepository userRegistrationRepository;
    private final DomainEventPublisher domainEventPublisher;

    UserRegistrationService(UserRegistrationFactory userRegistrationFactory, UserRegistrationRepository userRegistrationRepository, DomainEventPublisher domainEventPublisher) {
        this.userRegistrationFactory = userRegistrationFactory;
        this.userRegistrationRepository = userRegistrationRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public UserRegistrationId startNewUserRegistrationProcess(UserHandle userHandle, PhoneNumber phoneNumber) throws UserHandleAlreadyInUseException {
        UserRegistration userRegistration = this.userRegistrationFactory.newInstance(userHandle, phoneNumber);
        UserRegistrationId userRegistrationId = userRegistration.id();

        this.userRegistrationRepository.add(userRegistration);

        return userRegistrationId;
    }

    public void verifyPhoneNumber(UserRegistrationId userRegistrationId, VerificationCode verificationCode) throws UserRegistrationNotExistingException, PhoneNumberVerificationCodeInvalidException {
        UserRegistration userRegistration = this.userRegistrationRepository.get(userRegistrationId);
        userRegistration.verifyPhoneNumber(verificationCode);

        this.userRegistrationRepository.update(userRegistration);
    }

    public void completeUserRegistration(UserRegistrationId userRegistrationId, FullName fullName) throws UserRegistrationNotExistingException, PhoneNumberNotYetVerifiedException, UserRegistrationAlreadyCompletedException {
        UserRegistration userRegistration = this.userRegistrationRepository.get(userRegistrationId);
        userRegistration.complete(fullName);

        this.userRegistrationRepository.update(userRegistration);
        this.domainEventPublisher.publish(userRegistrationCompleted(userRegistration.id(), userRegistration.userHandle(), userRegistration.phoneNumber(), userRegistration.fullName()));
    }

}
