package kitchenpos;

import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class EntitySupporter {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    public MenuGroupRepository getMenuGroupRepository() {
        return menuGroupRepository;
    }

    public MenuProductRepository getMenuProductRepository() {
        return menuProductRepository;
    }

    public MenuRepository getMenuRepository() {
        return menuRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public OrderLineItemRepository getOrderLineItemRepository() {
        return orderLineItemRepository;
    }

    public OrderTableRepository getOrderTableRepository() {
        return orderTableRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public TableGroupRepository getTableGroupRepository() {
        return tableGroupRepository;
    }
}
