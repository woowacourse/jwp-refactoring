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
        return entitySupporter.getMenuGroupRepository().save(menuGroup);
    }

    public MenuProduct buildMenuProduct(final MenuProduct menuProduct) {
        return entitySupporter.getMenuProductRepository().save(menuProduct);
    }

    public Menu buildMenu(final Menu menu) {
        return entitySupporter.getMenuRepository().save(menu);
    }

    public Order buildOrder(final Order order) {
        return entitySupporter.getOrderRepository().save(order);
    }

    public OrderLineItem buildOrderLineItem(final OrderLineItem orderLineItem) {
        return entitySupporter.getOrderLineItemRepository().save(orderLineItem);
    }

    public OrderTable buildOrderTable(final OrderTable orderTable) {
        return entitySupporter.getOrderTableRepository().save(orderTable);
    }

    public Product buildProduct(final Product product) {
        return entitySupporter.getProductRepository().save(product);
    }

    public TableGroup buildTableGroup(final TableGroup tableGroup) {
        return entitySupporter.getTableGroupRepository().save(tableGroup);
    }
}
