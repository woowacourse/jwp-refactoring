package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.TableGroupCreateRequest;

public class RequestParser {

    public static TableGroupCreateRequest from(final List<OrderTable> entities) {
        final List<Long> ids = entities.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(ids);
    }

    public static OrderCreateRequest of(final OrderTable orderTable, final List<Menu> menus) {
        final List<OrderLineItemCreateRequest> orderLineItems = menus.stream()
                .map(menu -> new OrderLineItemCreateRequest(menu.getId(), 1L))
                .collect(Collectors.toList());
        return new OrderCreateRequest(orderTable.getId(), orderLineItems);
    }

    public static MenuCreateRequest of(final String name,
                                       final BigDecimal price,
                                       final MenuGroup menuGroup,
                                       final List<Product> products) {
        final List<MenuProductCreateRequest> menuProducts = products.stream()
                .map(product -> new MenuProductCreateRequest(product.getId(), 1L))
                .collect(Collectors.toList());
        return new MenuCreateRequest(name, price, menuGroup.getId(), menuProducts);
    }
}

