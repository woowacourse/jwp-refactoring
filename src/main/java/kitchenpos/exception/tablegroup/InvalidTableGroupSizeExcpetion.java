package kitchenpos.exception.tablegroup;

public class InvalidTableGroupSizeExcpetion extends RuntimeException {
    private static final String MESSAGE = "테이블 그룹은 최소 2개 이상이어야 합니다.";

    public InvalidTableGroupSizeExcpetion() {
        super(MESSAGE);
    }
}
