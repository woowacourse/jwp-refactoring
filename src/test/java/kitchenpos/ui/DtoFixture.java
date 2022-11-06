package kitchenpos.ui;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.application.dto.MenuGroupDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderTableDto;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.order.application.dto.TableGroupDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.product.domain.Product;

public class DtoFixture {

    public static ProductDto getProduct() {
        final Product product = new Product(1L, "productName", BigDecimal.valueOf(1000L));
        return ProductDto.from(product);
    }

    public static MenuGroupDto getMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup(1L, "menuGroupName");
        return MenuGroupDto.from(menuGroup);
    }

    public static MenuDto getMenuDto() {
        final Product product = new Product(1L, "productName", BigDecimal.valueOf(1000L));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, product, 1));
        final Menu menu = new Menu(1L, "menuName", BigDecimal.valueOf(1000L), 1L, menuProducts);
        return MenuDto.from(menu);
    }

    public static OrderTableDto getOrderTableDto(final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable(1L, null, 0, isEmpty);
        return OrderTableDto.from(orderTable);
    }

    public static OrderDto getOrderDto() {
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final Order order = new Order(1L, 2L, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItem));
        return OrderDto.from(order);
    }

    public static TableGroupDto getTableGroupDto() {
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 0, true);
        final OrderTable orderTable2 = new OrderTable(2L, 1L, 0, true);
        return new TableGroupDto(1L, LocalDateTime.now(),
                List.of(OrderTableDto.from(orderTable1), OrderTableDto.from(orderTable2)));
    }
}
