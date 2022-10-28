package kitchenpos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuCreateRequest.MenuProductCreateRequest;

public class MenuCreateRequestFixture {

    private static final String DEFAULT_NAME = "후라이드";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(10000);
    private static final Long DEFAULT_MENU_GROUP_ID = 1L;
    private static final List<MenuProductCreateRequest> DEFAULT_MENU_PRODUCTS = createMenuProducts(new MenuProductCreateRequest(1L, 1L));

    private MenuCreateRequestFixture() {
    }

    public static MenuCreateRequest createRequest(final String name, final BigDecimal price, final Long menuGroupId,
                                                  final List<MenuProductCreateRequest> menuProducts) {
        return new MenuCreateRequest(name, price, menuGroupId, menuProducts);
    }

    public static MenuCreateRequest createRequest(final BigDecimal price) {
        return createRequest(DEFAULT_NAME, price, DEFAULT_MENU_GROUP_ID, DEFAULT_MENU_PRODUCTS);
    }

    public static MenuCreateRequest createRequest(final Long menuGroupId) {
        return createRequest(DEFAULT_NAME, DEFAULT_PRICE, menuGroupId, DEFAULT_MENU_PRODUCTS);
    }

    public static List<MenuProductCreateRequest> createMenuProducts(final MenuProductCreateRequest... request) {
        return Arrays.stream(request)
                .collect(Collectors.toList());
    }
}
