package kitchenpos.fixture;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.TableGroup;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class ServiceDependencies {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderDao orderDao;

    public ServiceDependencies(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository, final MenuDao menuDao,
                               final MenuProductDao menuProductDao, final OrderTableDao orderTableDao,
                               final TableGroupDao tableGroupDao, final OrderLineItemDao orderLineItemDao,
                               final OrderDao orderDao) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderDao = orderDao;
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
        return menuDao.save(menu);
    }

    public Order save(final Order order) {
        return orderDao.save(order);
    }

    public OrderLineItem save(final OrderLineItem orderLineItem) {
       return orderLineItemDao.save(orderLineItem);
    }
}
