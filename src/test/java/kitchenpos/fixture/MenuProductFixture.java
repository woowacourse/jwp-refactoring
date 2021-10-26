package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static final MenuProduct 후라이드치킨_후라이드치킨;
    public static final MenuProduct 양념치킨_양념치킨;
    public static final MenuProduct 반반치킨_반반치킨;
    public static final MenuProduct 통구이_통구이;
    public static final MenuProduct 간장치킨_간장치킨;
    public static final MenuProduct 순살치킨_순살치킨;
    public static final MenuProduct 후라이드후라이드_후라이드치킨;
    public static final MenuProduct 후라이드후라이드_후라이드치킨_NO_KEY;

    static {
        후라이드치킨_후라이드치킨 = newInstance(1L, 1L, 1L, 1);
        양념치킨_양념치킨 = newInstance(2L, 2L, 2L, 1);
        반반치킨_반반치킨 = newInstance(3L, 3L, 3L, 1);
        통구이_통구이 = newInstance(4L, 4L, 4L, 1);
        간장치킨_간장치킨 = newInstance(5L, 5L, 5L, 1);
        순살치킨_순살치킨 = newInstance(6L, 6L, 6L, 1);
        후라이드후라이드_후라이드치킨 = newInstance(7L, 7L, 1L, 2);
        후라이드후라이드_후라이드치킨_NO_KEY = newInstance(null, null, 1L, 2);
    }

    private static MenuProduct newInstance(Long seq, Long menuId, Long productId, long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
