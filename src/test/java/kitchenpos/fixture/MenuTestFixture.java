package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.ui.dto.MenuProductDto;
import kitchenpos.menu.ui.dto.MenuRequest;

@SuppressWarnings("NonAsciiCharacters")
public enum MenuTestFixture {

    떡볶이("떡볶이", BigDecimal.valueOf(10000)),
    ;

    private final String name;
    private final BigDecimal price;

    MenuTestFixture(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Menu toEntity(final long menuGroupId, final List<MenuProduct> menuProducts) {
        return toEntity(price, menuGroupId, menuProducts);
    }

    public Menu toEntity(final BigDecimal price, final long menuGroupId, final List<MenuProduct> menuProducts) {
        Menu menu = new Menu(null, name, price, menuGroupId);
        menu.addMenuProducts(menuProducts);
        return menu;
    }

    public MenuRequest toRequest(final long menuGroupId, final List<MenuProduct> menuProducts) {
        return new MenuRequest(name, price.longValue(), menuGroupId, toMenuProductDto(menuProducts));
    }

    public MenuRequest toRequest(final long price, final long menuGroupId, final List<MenuProduct> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, toMenuProductDto(menuProducts));
    }

    private List<MenuProductDto> toMenuProductDto(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(it -> new MenuProductDto(it.getSeq(), it.getMenuId(), it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
