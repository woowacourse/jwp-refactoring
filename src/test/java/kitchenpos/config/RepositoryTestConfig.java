package kitchenpos.config;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;

@Sql("/truncate.sql")
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
}
