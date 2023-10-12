package kitchenpos.fixture;

import kitchenpos.application.menu.dto.MenuCreateRequest;
import kitchenpos.application.menu.dto.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_생성(final String name,
                             final BigDecimal price,
                             final Long menuGroupId,
                             final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static MenuCreateRequest 메뉴_생성_요청(final Menu menu) {
        List<MenuProductCreateRequest> menuProductCreateRequests = menu.getMenuProducts()
                .stream()
                .map(it -> new MenuProductCreateRequest(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuCreateRequest(menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductCreateRequests
        );
    }
}
