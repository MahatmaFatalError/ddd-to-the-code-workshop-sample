package com.github.cstettler.dddttc.accounting.infrastructure.event;

import com.github.cstettler.dddttc.accounting.application.WalletService;
import com.github.cstettler.dddttc.accounting.domain.Booking;
import com.github.cstettler.dddttc.accounting.domain.BookingAlreadyBilledException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.github.cstettler.dddttc.accounting.domain.Booking.booking;
import static com.github.cstettler.dddttc.accounting.domain.UserId.userId;

@Component
class BookingCompletedMessageListener {

    private final WalletService walletService;

    BookingCompletedMessageListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @JmsListener(destination = "rental/booking-completed")
    public void onUserRegistrationCompleted(BookingCompletedMessage message) {
        try {
            String userIdValue = message.userId.value;
            LocalDateTime startedAt = message.bikeUsage.startedAt;
            long durationInSeconds = message.bikeUsage.durationInSeconds;

            Booking booking = booking(userId(userIdValue), startedAt, durationInSeconds);

            this.walletService.billBookingFee(booking);
        } catch (BookingAlreadyBilledException e) {
            // ignored
        }
    }


    public static class BookingCompletedMessage {

        UserId userId;
        BikeUsage bikeUsage;
    }


    public static class UserId {

        String value;
    }


    public static class BikeUsage {

        LocalDateTime startedAt;
        long durationInSeconds;

    }

}
