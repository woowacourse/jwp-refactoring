package kitchenpos.exception;

public class GroupTableNotEnoughException extends IllegalArgumentException {

    private static final String MESSAGE = "Table Group은 2개 이상의 테이블로 생성할 수 있습니다.";

    public GroupTableNotEnoughException() {
        super(MESSAGE);
    }
}
