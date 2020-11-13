package kitchenpos.exception;

public class MenuGroupNotFoundException extends RuntimeException {
    public MenuGroupNotFoundException(Long menuGroupId) {
        super("아이디가 " + menuGroupId + "인 MenuGroup을 찾을 수 없습니다!");
    }
}
