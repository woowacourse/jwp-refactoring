package kitchenpos.support.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class ServiceDependencies {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public ServiceDependencies(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository,
                               final MenuRepository menuRepository, final OrderTableRepository orderTableRepository,
                               final TableGroupRepository tableGroupRepository, final OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    public MenuGroup save(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    public Product save(final Product product) {
        return productRepository.save(product);
    }

    public OrderTable save(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public TableGroup save(final TableGroup tableGroup) {
        return tableGroupRepository.save(tableGroup);
    }

    public Menu save(final Menu menu) {
        return menuRepository.save(menu);
    }

    public Order save(final Order order) {
        return orderRepository.save(order);
    }
}
