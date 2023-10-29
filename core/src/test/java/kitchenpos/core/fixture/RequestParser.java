package kitchenpos.core.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.domain.menu.Menu;
import kitchenpos.core.domain.menu.MenuGroup;
import kitchenpos.core.domain.ordertable.OrderTable;
import kitchenpos.core.domain.product.Product;
import kitchenpos.core.dto.request.IdForRequest;
import kitchenpos.core.dto.request.MenuCreateRequest;
import kitchenpos.core.dto.request.MenuProductCreateRequest;
import kitchenpos.core.dto.request.OrderCreateRequest;
import kitchenpos.core.dto.request.OrderLineItemCreateRequest;
import kitchenpos.core.dto.request.ProductCreateRequest;
import kitchenpos.core.dto.request.TableGroupCreateRequest;

public class RequestParser {

    public static ProductCreateRequest from(final Product product) {
        return new ProductCreateRequest(product.getName(), product.getPrice().value());
    }

    public static TableGroupCreateRequest from(final List<OrderTable> entities) {
        final List<IdForRequest> ids = entities.stream()
                .map(OrderTable::getId)
                .map(IdForRequest::new)
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

