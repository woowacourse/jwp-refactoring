package com.kitchenpos.exception;

public class ProductPriceMoreLessException extends RuntimeException {

    public ProductPriceMoreLessException() {
        super("상품 가격의 총합이 더 작습니다.");
    }
}
