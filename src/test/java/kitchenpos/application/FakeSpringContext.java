package kitchenpos.application;

import kitchenpos.domain.menu.MenuDao;
import kitchenpos.domain.menu.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProductDao;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.ProductDao;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.domain.order.OrderDao;
import kitchenpos.domain.order.OrderLineItemDao;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.domain.table.OrderTableDao;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupDao;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.infrastructure.MenuFakeDao;
import kitchenpos.infrastructure.MenuGroupFakeDao;
import kitchenpos.infrastructure.MenuProductFakeDao;
import kitchenpos.infrastructure.OrderFakeDao;
import kitchenpos.infrastructure.OrderLineItemFakeDao;
import kitchenpos.infrastructure.OrderTableFakeDao;
import kitchenpos.infrastructure.ProductFakeDao;
import kitchenpos.infrastructure.TableGroupFakeDao;
import kitchenpos.infrastructure.menu.MenuGroupRepositoryImpl;
import kitchenpos.infrastructure.menu.MenuRepositoryImpl;
import kitchenpos.infrastructure.menu.ProductRepositoryImpl;
import kitchenpos.infrastructure.order.OrderRepositoryImpl;
import kitchenpos.infrastructure.table.OrderTableRepositoryImpl;
import kitchenpos.infrastructure.table.TableGroupRepositoryImpl;

public abstract class FakeSpringContext {

    protected final OrderValidator orderValidator = new OrderValidator();

    protected final MenuDao menuDao = new MenuFakeDao();
    protected final MenuGroupDao menuGroupDao = new MenuGroupFakeDao();
    protected final MenuProductDao menuProductDao = new MenuProductFakeDao();
    protected final OrderDao orderDao = new OrderFakeDao();
    protected final OrderLineItemDao orderLineItemDao = new OrderLineItemFakeDao();
    protected final OrderTableDao orderTableDao = new OrderTableFakeDao();
    protected final ProductDao productDao = new ProductFakeDao();
    protected final TableGroupDao tableGroupDao = new TableGroupFakeDao();

    protected final MenuRepository menus = new MenuRepositoryImpl(menuDao, menuProductDao);
    protected final ProductRepository products = new ProductRepositoryImpl(productDao);
    protected final MenuGroupRepository menuGroups = new MenuGroupRepositoryImpl(menuGroupDao);
    protected final OrderTableRepository orderTables = new OrderTableRepositoryImpl(orderDao, orderTableDao);
    protected final OrderRepository orders = new OrderRepositoryImpl(orderDao, orderLineItemDao);
    protected final TableGroupRepository tableGroups = new TableGroupRepositoryImpl(tableGroupDao, orderTableDao, orderDao);
}
