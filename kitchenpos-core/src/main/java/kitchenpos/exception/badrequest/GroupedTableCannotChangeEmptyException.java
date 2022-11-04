package kitchenpos.exception.badrequest;

public class GroupedTableCannotChangeEmptyException extends BadRequestException {

    public GroupedTableCannotChangeEmptyException() {
        super("이미 단체 지정된 테이블의 빈 테이블 여부를 변경할 수 없습니다.");
    }
}
