package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class TestFixtureFactory {

    private TestFixtureFactory() {
    }

    public static Product 상품을_생성한다(final String name, final BigDecimal price) {
        return new Product(null, name, price);
    }

    public static MenuGroup 메뉴_그룹을_생성한다(final String name) {
        return new MenuGroup(null, name);
    }

    public static Menu 메뉴를_생성한다(final String name, final BigDecimal price, final Long menuGroupId,
                                final List<MenuProduct> menuProducts) {
        return new Menu(null, name, new Price(price), menuGroupId, menuProducts);
    }

    public static MenuProduct 메뉴_상품을_생성한다(final Long productId, final long quantity, final Price price) {
        return new MenuProduct(null, null, productId, quantity, price);
    }

    public static Order 주문을_생성한다(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                                 List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderLineItem 주문_항목을_생성한다(final Order order, final Long menuId, final long quantity) {
        return new OrderLineItem(null, order, menuId, quantity);
    }

    public static OrderTable 주문_테이블을_생성한다(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable id를_가진_주문_테이블을_생성한다(final Long id, final TableGroup tableGroup, final int numberOfGuests,
                                                 final boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static TableGroup 단체_지정을_생성한다(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        return new TableGroup(null, createdDate, orderTables);
    }
}
