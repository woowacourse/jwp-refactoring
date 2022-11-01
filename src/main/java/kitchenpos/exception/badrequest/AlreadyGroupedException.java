package kitchenpos.exception.badrequest;

public class AlreadyGroupedException extends BadRequestException {

    public AlreadyGroupedException() {
        super("이미 단체 지정한 테이블을 단체 지정할 수 없습니다.");
    }
}
