package kitchenpos.exception;

public class InvalidOrderTableToGroupException extends RuntimeException {
    public InvalidOrderTableToGroupException() {
        super("테이블 그룹으로 만들 수 없는 주문 테이블입니다.");
    }
}
