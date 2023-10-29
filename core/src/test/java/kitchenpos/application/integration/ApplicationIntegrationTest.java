package kitchenpos.application.integration;

import kitchenpos.common.domain.Money;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroups.application.MenuGroupService;
import kitchenpos.menugroups.dto.CreateMenuGroupRequest;
import kitchenpos.menugroups.dto.MenuGroupResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.CreateProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.CreateOrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroups.application.TableGroupService;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Sql("/db/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ApplicationIntegrationTest {
    @Autowired
    protected ProductService productService;
    @Autowired
    protected MenuService menuService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected TableGroupService tableGroupService;
    @Autowired
    protected TableService tableService;


    protected ProductResponse createProduct(final String name, final BigDecimal price) {
        return productService.create(CreateProductRequest.of(name, price.longValue()));
    }

    protected MenuGroupResponse createMenuGroup(final String name) {
        return menuGroupService.create(CreateMenuGroupRequest.of(name));
    }

    protected MenuResponse createMenu(final String name, final Money price) {
        final MenuGroupResponse menuGroup = createMenuGroup("치킨");
        final ProductResponse product1 = createProduct("후라이드", BigDecimal.valueOf(16000));
        final ProductResponse product2 = createProduct("양념치킨", BigDecimal.valueOf(16000));
        final MenuProductRequest menuProduct1 = MenuProductRequest.of(product1.getId(), 1);
        final MenuProductRequest menuProduct2 = MenuProductRequest.of(product2.getId(), 1);
        return menuService.create(CreateMenuRequest.of(name, price.getPrice().longValue(), menuGroup.getId(), List.of(menuProduct1, menuProduct2)));
    }

    protected OrderResponse createOrder(final long orderTableId) {
        final MenuResponse menu = createMenu("후라이드", Money.valueOf(16000));
        return orderService.create(CreateOrderRequest.of(orderTableId, List.of(OrderLineItemRequest.of(menu.getId(), 1))));
    }

    protected OrderTableResponse createOrderTable(final int numberOfGuests, final boolean orderable) {
        return tableService.create(CreateOrderTableRequest.of(numberOfGuests, orderable));
    }
}