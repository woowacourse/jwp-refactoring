package kitchenpos;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.TableGroup;
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
