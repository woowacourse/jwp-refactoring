package kitchenpos.exception;

public class IllegalPriceException extends IllegalArgumentException {

    public IllegalPriceException() {
        super("가격이 없거나 0원보다 작습니다");
    }
}
