package kitchenpos.table.exception;

public class IllegalOrderTableGuestNumberException extends OrderTableExcpetion {
    private final static String error = "잘못된 주문 테이블 게스트 숫자 설정입니다.";
    public IllegalOrderTableGuestNumberException() {
        super(error);
    }
}
