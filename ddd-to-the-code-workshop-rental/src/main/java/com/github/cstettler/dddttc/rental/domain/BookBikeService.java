package com.github.cstettler.dddttc.rental.domain;

import com.github.cstettler.dddttc.stereotype.DomainService;

import java.time.Clock;

@DomainService
public class BookBikeService {

    private final BikeRepository bikeRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public BookBikeService(BikeRepository bikeRepository, BookingRepository bookingRepository, UserRepository userRepository, Clock clock) {
        this.bikeRepository = bikeRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public void bookBike(NumberPlate numberPlate, UserId userId) throws BikeNotExistingException, UserNotExistingException, BikeAlreadyBookedException {
        Bike bike = this.bikeRepository.get(numberPlate);
        User user = this.userRepository.get(userId);

        Booking booking = bike.bookBikeFor(user, this.clock);
        this.bookingRepository.add(booking);
    }

}
