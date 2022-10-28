package kitchenpos.exception;

public class MenuProductAmountException extends RuntimeException {
    public MenuProductAmountException() {
        super("제품 갯수에 따른 가격 합 구하기에 실패했습니다.");
    }
}
