package kitchenpos.exception.badrequest;

public class MenuGroupNotExistsException extends BadRequestException {

    public MenuGroupNotExistsException() {
        super("지정한 메뉴 그룹이 존재하지 않습니다.");
    }
}
