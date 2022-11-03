package kitchenpos.exception;

public class NotFoundMenuGroupException extends IllegalArgumentException {

    public NotFoundMenuGroupException() {
        super("해당 메뉴 그룹이 존재하지 않습니다");
    }
}
