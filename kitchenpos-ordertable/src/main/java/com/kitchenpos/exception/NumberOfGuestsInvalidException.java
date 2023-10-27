package com.kitchenpos.exception;

public class NumberOfGuestsInvalidException extends RuntimeException {

    public NumberOfGuestsInvalidException() {
        super("손님의 수는 0명 이상이어야 합니다.");
    }
}
