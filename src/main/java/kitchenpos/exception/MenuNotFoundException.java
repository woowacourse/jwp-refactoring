package kitchenpos.exception;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(Long menuId) {
        super("아이디가 " + menuId + "인 Menu를 찾을 수 없습니다!");
    }
}
