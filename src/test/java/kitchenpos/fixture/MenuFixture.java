package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_망고치킨_17000원_신메뉴() {
        return new Menu("망고 치킨", BigDecimal.valueOf(17000), MenuGroupFixture.메뉴그룹_신메뉴(),
                List.of(MenuProductFixture.메뉴상품_망고_2개(), MenuProductFixture.메뉴상품_치킨_1개()));
    }

    public static Menu 메뉴_망고치킨_17000원(final MenuGroup menuGroup, final MenuProduct... menuProducts) {
        return new Menu("망고 치킨", BigDecimal.valueOf(17000), menuGroup, List.of(menuProducts));
    }

    public static Menu 메뉴_망고치킨_N원_신메뉴(final int price) {
        return new Menu("망고 치킨", BigDecimal.valueOf(price), MenuGroupFixture.메뉴그룹_신메뉴(),
                List.of(MenuProductFixture.메뉴상품_망고_2개(), MenuProductFixture.메뉴상품_치킨_1개()));
    }

    public static MenuCreateRequest 메뉴요청_생성(final Menu menu) {
        final var menuProductRequests = menu.getMenuProducts().stream()
                .map(it -> new MenuProductRequest(it.getProduct().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new MenuCreateRequest(menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(), menuProductRequests);
    }

    public static MenuCreateRequest 메뉴요청_망고치킨_N원_생성(final int price, final MenuGroup menuGroup, final MenuProduct... menuProducts) {
        final var menuProductRequests = Arrays.stream(menuProducts)
                .map(it -> new MenuProductRequest(it.getProduct().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new MenuCreateRequest("망고 치킨", BigDecimal.valueOf(price), menuGroup.getId(), menuProductRequests);
    }
}
