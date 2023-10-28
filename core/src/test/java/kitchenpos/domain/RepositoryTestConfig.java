package kitchenpos.domain;

import kitchenpos.domain.fixture.OrderMenuFixture;
import kitchenpos.domain.fixture.TableGroupFixture;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderMenu;
import kitchenpos.order.OrderStatus;
import kitchenpos.product.Product;
import kitchenpos.table.OrderTable;
import kitchenpos.table.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static kitchenpos.domain.fixture.MenuFixture.menu;
import static kitchenpos.domain.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.domain.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.domain.fixture.OrderFixture.order;
import static kitchenpos.domain.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.domain.fixture.OrderTableFixture.orderTable;
import static kitchenpos.domain.fixture.ProductFixture.product;

@DataJpaTest
public abstract class RepositoryTestConfig {

    @Autowired
    protected EntityManager em;

    protected MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = menuGroup(name);
        em.persist(menuGroup);
        return menuGroup;
    }

    protected Menu createMenu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        final Menu menu = menu(name, price, menuGroup, new ArrayList<>());
        em.persist(menu);
        return menu;
    }

    protected OrderMenu createOrderMenu(final Long menuId, final String name, final BigDecimal price) {
        final OrderMenu orderMenu = OrderMenuFixture.orderMenu(menuId, name, price);
        em.persist(orderMenu);
        return orderMenu;
    }

    protected Product createProduct(final String name, final BigDecimal price) {
        final Product product = product(name, price);
        em.persist(product);
        return product;
    }

    protected MenuProduct createMenuProduct(final Menu menu, final Long productId, final long price) {
        final MenuProduct menuProduct = menuProduct(menu, productId, price);
        em.persist(menuProduct);
        return menuProduct;
    }

    protected TableGroup createTableGroup(final LocalDateTime createdDate) {
        final TableGroup tableGroup = TableGroupFixture.tableGroup(createdDate);
        em.persist(tableGroup);
        return tableGroup;
    }

    protected OrderTable createOrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = orderTable(tableGroup, numberOfGuests, empty);
        em.persist(orderTable);
        return orderTable;
    }

    protected Order createOrder(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        final Order order = order(orderTableId, orderStatus, orderedTime, new ArrayList<>());
        em.persist(order);
        return order;
    }

    protected OrderLineItem createOrderLineItem(final Long orderId, final OrderMenu orderMenu, final long quantity) {
        final OrderLineItem orderLineItem = orderLineItem(orderId, orderMenu, quantity);
        em.persist(orderLineItem);
        return orderLineItem;
    }
}
