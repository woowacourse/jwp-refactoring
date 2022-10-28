package kitchenpos.table.domain;

public class InvalidTableGroupJoinException extends RuntimeException {

    public InvalidTableGroupJoinException() {
        super("단체 지정을 할 수 없는 주문 테이블입니다.");
    }
}
