package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TestFixtureBuilder {

    @Autowired
    private EntitySupporter entitySupporter;

    public MenuGroup buildMenuGroup(final MenuGroup menuGroup) {
        return entitySupporter.getMenuGroupDao().save(menuGroup);
    }

    public MenuProduct buildMenuProduct(final MenuProduct menuProduct) {
        return entitySupporter.getMenuProductDao().save(menuProduct);
    }

    public Menu buildMenu(final Menu menu) {
        return entitySupporter.getMenuDao().save(menu);
    }

    public Order buildOrder(final Order order) {
        return entitySupporter.getOrderDao().save(order);
    }

    public OrderLineItem buildOrderLineItem(final OrderLineItem orderLineItemDao) {
        return entitySupporter.getOrderLineItemDao().save(orderLineItemDao);
    }

    public OrderTable buildOrderTable(final OrderTable orderTable) {
        return entitySupporter.getOrderTableDao().save(orderTable);
    }

    public Product buildProduct(final Product product) {
        return entitySupporter.getProductDao().save(product);
    }

    public TableGroup buildTableGroup(final TableGroup tableGroupDao) {
        return entitySupporter.getTableGroupDao().save(tableGroupDao);
    }
}
