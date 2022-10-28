package kitchenpos.fixture;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.TableGroup;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class ServiceDependencies {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderRepository orderRepository;

    public ServiceDependencies(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository,
                               final MenuRepository menuRepository, final MenuProductRepository menuProductRepository,
                               final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao,
                               final OrderLineItemDao orderLineItemDao, final OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderRepository = orderRepository;
    }

    public MenuGroup save(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    public Product save(final Product product) {
        return productRepository.save(product);
    }

    public OrderTable save(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    public TableGroup save(final TableGroup tableGroup) {
        return tableGroupDao.save(tableGroup);
    }

    public Menu save(final Menu menu) {
        return menuRepository.save(menu);
    }

    public Order save(final Order order) {
        return orderRepository.save(order);
    }

    public OrderLineItem save(final OrderLineItem orderLineItem) {
        return orderLineItemDao.save(orderLineItem);
    }
}
