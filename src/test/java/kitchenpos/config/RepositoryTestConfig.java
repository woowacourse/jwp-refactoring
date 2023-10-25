package kitchenpos.config;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;

@Sql("/truncate.sql")
@Import(JpaAuditingConfig.class)
@DataJpaTest
public abstract class RepositoryTestConfig {

    @Autowired
    protected EntityManager em;

    protected Product persistProduct(final Product product) {
        em.persist(product);
        return product;
    }

    protected MenuProduct persistMenuProduct(final MenuProduct menuProduct) {
        em.persist(menuProduct);
        return menuProduct;
    }

    protected Menu persistMenu(final Menu menu) {
        em.persist(menu);
        return menu;
    }

    protected MenuGroup persistMenuGroup(final MenuGroup menuGroup) {
        em.persist(menuGroup);
        return menuGroup;
    }

    protected OrderLineItem persistOrderLineItem(final OrderLineItem orderLineItem) {
        em.persist(orderLineItem);
        return orderLineItem;
    }

    protected Order persistOrder(final Order order) {
        em.persist(order);
        return order;
    }

    protected OrderTable persistOrderTable(final OrderTable orderTable) {
        em.persist(orderTable);
        return orderTable;
    }

    protected TableGroup persistTableGroup(final TableGroup tableGroup) {
        em.persist(tableGroup);
        return tableGroup;
    }
}
