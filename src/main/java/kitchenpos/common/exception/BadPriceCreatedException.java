package kitchenpos.common.exception;

public class BadPriceCreatedException extends InvalidRequestException {

    public BadPriceCreatedException() {
        super("가격이 null 이거나 최솟 값 미만입니다.");
    }
}
