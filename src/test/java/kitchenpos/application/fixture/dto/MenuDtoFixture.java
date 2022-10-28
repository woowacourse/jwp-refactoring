package kitchenpos.application.fixture.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;

public class MenuDtoFixture {

    public static MenuRequest createMenuRequest(final String name, final long price, final MenuGroup menuGroup,
                                                final Product... products) {
        final List<MenuProductRequest> menuProductRequests = Arrays.stream(products)
                .map(it -> new MenuProductRequest(it.getId(), 1))
                .collect(Collectors.toList());

        return new MenuRequest(name, BigDecimal.valueOf(price), menuGroup.getId(), menuProductRequests);
    }
}
