package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateDto;
import kitchenpos.dto.MenuProductCreateDto;

public class MenuFixture {

    public static MenuCreateDto 메뉴_생성_요청(final String name, final int price, final Long menuGroupId,
                                         final List<MenuProduct> menuProducts) {
        List<MenuProductCreateDto> menuProductDtos = menuProducts.stream()
                .map(it -> new MenuProductCreateDto(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new MenuCreateDto(name, BigDecimal.valueOf(price), menuGroupId, menuProductDtos);
    }

    public static Menu 메뉴(String name, int price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public static Menu 메뉴(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
