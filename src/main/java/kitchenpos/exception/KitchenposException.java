package kitchenpos.exception;

public class KitchenposException extends IllegalArgumentException{
    public static final String ILLEGAL_PRICE = "옳지 않은 가격입니다.";
    public static final String ILLEGAL_MENU_GROUP_ID = "존재하지 않는 메뉴그룹 아이디입니다.";
    public static final String ILLEGAL_PRODUCT_ID = "존재하지 않는 메뉴그룹 아이디입니다.";
    public static final String IMPOSSIBLE_MENU_PRICE = "메뉴 가격이 상품 가격의 총합보다 비쌀 수 없습니다.";

    public KitchenposException(String s) {
        super(s);
    }
}
