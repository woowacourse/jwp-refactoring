package kitchenpos;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {
    private static Product 후라이드치킨 = new Product(1L, "후라이드치킨", 17000);
    private static Product 양념치킨 = new Product(2L, "양념치킨", 17000);
    private static Product 간장치킨 = new Product(3L, "간장치킨", 17000);
    private static Product 강정치킨 = new Product(4L, "강정치킨", 16000);

    public static MenuProduct 후라이드치킨_한마리_메뉴상품 = new MenuProduct(후라이드치킨.getId(), 1);
    public static MenuProduct 양념치킨_한마리_메뉴상품 = new MenuProduct(양념치킨.getId(), 1);
    public static MenuProduct 간장치킨_두마리_메뉴상품 = new MenuProduct(간장치킨.getId(), 2);
    public static MenuProduct 강정치킨_한마리_메뉴상품 = new MenuProduct(강정치킨.getId(), 1);
}
