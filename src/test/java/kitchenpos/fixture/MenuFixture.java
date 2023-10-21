package kitchenpos.fixture;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_생성(final String name,
                             final Long price,
                             final Long menuGroupId,
                             final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static MenuCreateRequest 메뉴_생성_요청(
            final String name,
            final Long price,
            final Long menuGroupId,
            final List<MenuProductCreateRequest> menuProductCreateRequests
    ) {
        return new MenuCreateRequest(name,
                price,
                menuGroupId,
                menuProductCreateRequests
        );
    }

    public static MenuCreateRequest 메뉴_생성_요청(final String name,
                                             final Long price,
                                             final MenuGroup menuGroup,
                                             final List<MenuProduct> menuProducts) {
        List<MenuProductCreateRequest> menuProductCreateRequests = menuProducts.stream()
                .map(it -> new MenuProductCreateRequest(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuCreateRequest(
                name,
                price,
                menuGroup.getId(),
                menuProductCreateRequests
        );
    }
}
