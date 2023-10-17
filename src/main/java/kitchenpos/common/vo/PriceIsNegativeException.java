package kitchenpos.common.vo;

public class PriceIsNegativeException extends RuntimeException {

    private static final String MESSAGE = "가격이 음수일 수 없습니다.";

    public PriceIsNegativeException() {
        super(MESSAGE);
    }
}
