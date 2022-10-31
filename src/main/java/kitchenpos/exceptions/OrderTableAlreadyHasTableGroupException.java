package kitchenpos.exceptions;

public class OrderTableAlreadyHasTableGroupException extends RuntimeException {

    public OrderTableAlreadyHasTableGroupException() {
        super("테이블이 이미 그룹으로 지정되어 있습니다.");
    }
}
