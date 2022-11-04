package kitchenpos.exception.badrequest;

public class TableGroupNotExistsException extends BadRequestException {

    public TableGroupNotExistsException() {
        super("단체 지정이 존재하지 않습니다.");
    }
}
