package com.github.cstettler.dddttc.rental.application;

import com.github.cstettler.dddttc.rental.domain.BikeAlreadyBookedException;
import com.github.cstettler.dddttc.rental.domain.BikeNotExistingException;
import com.github.cstettler.dddttc.rental.domain.BookBikeService;
import com.github.cstettler.dddttc.rental.domain.Booking;
import com.github.cstettler.dddttc.rental.domain.BookingId;
import com.github.cstettler.dddttc.rental.domain.BookingRepository;
import com.github.cstettler.dddttc.rental.domain.NumberPlate;
import com.github.cstettler.dddttc.rental.domain.UserId;
import com.github.cstettler.dddttc.rental.domain.UserNotExistingException;
import com.github.cstettler.dddttc.stereotype.ApplicationService;

import java.time.Clock;
import java.util.Collection;

@ApplicationService
public class BookingService {

    private final BookBikeService bookBikeService;
    private final BookingRepository bookingRepository;
    private final Clock clock;

    BookingService(BookBikeService bookBikeService, BookingRepository bookingRepository, Clock clock) {
        this.bookBikeService = bookBikeService;
        this.bookingRepository = bookingRepository;
        this.clock = clock;
    }

    public void bookBike(NumberPlate numberPlate, UserId userId) throws BikeNotExistingException, UserNotExistingException, BikeAlreadyBookedException {
        this.bookBikeService.bookBike(numberPlate, userId);
    }

    public void returnBike(BookingId bookingId) {
        Booking booking = this.bookingRepository.get(bookingId);
        booking.returnBike(this.clock);

        this.bookingRepository.update(booking);
    }

    public Collection<Booking> listBookings() {
        return this.bookingRepository.findAll();
    }

}
