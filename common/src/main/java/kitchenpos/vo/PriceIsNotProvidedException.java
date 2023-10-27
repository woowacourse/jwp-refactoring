package kitchenpos.vo;

public class PriceIsNotProvidedException extends RuntimeException {

    private static final String MESSAGE = "상품 가격이 제공되지 않았습니다.";

    public PriceIsNotProvidedException() {
        super(MESSAGE);
    }
}
