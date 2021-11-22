package kitchenpos.exception;

public class NotFoundTableGroupException extends KitchenPosException {

    private static final String NOT_FOUND_TABLE_GROUP = "테이블 그룹을 찾을 수 없습니다.";

    public NotFoundTableGroupException() {
        super(NOT_FOUND_TABLE_GROUP);
    }
}
