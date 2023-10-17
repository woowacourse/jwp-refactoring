package kitchenpos;

import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
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
