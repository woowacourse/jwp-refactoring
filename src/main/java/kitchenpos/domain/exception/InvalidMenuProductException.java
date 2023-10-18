package kitchenpos.domain.exception;

public class InvalidMenuProductException extends IllegalArgumentException {

    public InvalidMenuProductException() {
        super("메뉴에 포함되는 메뉴 상품이 존재하지 않습니다.");
    }
}
