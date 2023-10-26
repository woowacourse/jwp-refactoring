package kitchenpos.repository;

import kitchenpos.application.fixture.TableGroupFixture;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static kitchenpos.application.fixture.MenuFixture.menu;
import static kitchenpos.application.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.application.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.application.fixture.OrderFixture.order;
import static kitchenpos.application.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.application.fixture.OrderTableFixture.orderTable;
import static kitchenpos.application.fixture.ProductFixture.product;

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
        final TableGroup tableGroup = TableGroupFixture.tableGroup(createdDate, new ArrayList<>());
        em.persist(tableGroup);
        return tableGroup;
    }

    protected OrderTable createOrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = orderTable(tableGroup, numberOfGuests, empty);
        em.persist(orderTable);
        return orderTable;
    }

    protected Order createOrder(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        final Order order = order(orderTable, orderStatus, orderedTime, new ArrayList<>());
        em.persist(order);
        return order;
    }

    protected OrderLineItem createOrderLineItem(final Order order, final Menu menu, final long quantity) {
        final OrderLineItem orderLineItem = orderLineItem(order, menu, quantity);
        em.persist(orderLineItem);
        return orderLineItem;
    }
}
