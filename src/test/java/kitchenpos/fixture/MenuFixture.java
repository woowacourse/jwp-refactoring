package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateDto;
import kitchenpos.dto.MenuProductCreateDto;
import kitchenpos.domain.Price;

public class MenuFixture {

    public static MenuCreateDto 메뉴_생성_요청(final String name, final int price, final Long menuGroupId,
                                         final List<MenuProduct> menuProducts) {
        List<MenuProductCreateDto> menuProductDtos = menuProducts.stream()
                .map(it -> new MenuProductCreateDto(it.getProduct().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new MenuCreateDto(name, BigDecimal.valueOf(price), menuGroupId, menuProductDtos);
    }

    public static Menu 메뉴(final String name, final int price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        return new Menu(name, new Price(BigDecimal.valueOf(price)), menuGroup, menuProducts);
    }
}
