package kitchenpos.domain.exception;

public class InvalidTableGroupException extends IllegalArgumentException {

    public InvalidTableGroupException() {
        super("단체 지정이 된 주문 테이블의 상태는 변경할 수 없습니다.");
    }
}
