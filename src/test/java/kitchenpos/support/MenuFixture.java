package kitchenpos.support;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu.application.dto.MenuRequestDto;
import kitchenpos.product.domain.Product;

public class MenuFixture {

    private static final Long QUANTITY = 1L;

    public static MenuRequestDto 메뉴_생성(final String name,
                                       final BigDecimal price,
                                       final Long menuGroupId,
                                       final Product... products) {
        return new MenuRequestDto(name, price, menuGroupId, makeMenuProducts(products));
    }

    private static List<MenuProductDto> makeMenuProducts(final Product[] products) {
        return Arrays.stream(products)
                .map(it -> new MenuProductDto(null, null, it.getId(), QUANTITY))
                .collect(Collectors.toList());
    }

}
