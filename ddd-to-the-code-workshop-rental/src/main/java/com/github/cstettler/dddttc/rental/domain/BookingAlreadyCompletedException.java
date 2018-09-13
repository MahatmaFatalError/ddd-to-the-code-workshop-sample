package com.github.cstettler.dddttc.rental.domain;

import com.github.cstettler.dddttc.stereotype.BusinessException;

@BusinessException
public class BookingAlreadyCompletedException extends RuntimeException {

    private BookingAlreadyCompletedException(BookingId bookingId) {
        super("booking '" + bookingId.value() + "' has already been completed");
    }

    public static BookingAlreadyCompletedException bookingAlreadyCompleted(BookingId bookingId) {
        return new BookingAlreadyCompletedException(bookingId);
    }

}
