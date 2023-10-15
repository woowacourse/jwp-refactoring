package kitchenpos;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class EntitySupporter {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    public MenuGroupDao getMenuGroupDao() {
        return menuGroupDao;
    }

    public MenuProductDao getMenuProductDao() {
        return menuProductDao;
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public OrderLineItemDao getOrderLineItemDao() {
        return orderLineItemDao;
    }

    public OrderTableDao getOrderTableDao() {
        return orderTableDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public TableGroupDao getTableGroupDao() {
        return tableGroupDao;
    }
}
