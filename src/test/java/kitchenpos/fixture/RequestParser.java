package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.request.IdForRequest;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;

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

