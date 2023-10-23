package kitchenpos.exception.menuException;

public class MenuNotFoundException extends MenuExcpetion {
    public static final String error = "메뉴를 찾을 수 없습니다.";
    public MenuNotFoundException() {
        super(error);
    }
}
