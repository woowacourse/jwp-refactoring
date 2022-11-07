package kitchenpos.order.dao;

import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenus;
import kitchenpos.order.domain.OrderProducts;
import org.springframework.stereotype.Repository;

@Repository
public class OrderMenuRepository implements OrderMenuDao {

    private final JdbcTemplateOrderMenuDao jdbcTemplateOrderMenuDao;
    private final JdbcTemplateOrderProductDao jdbcTemplateOrderProductDao;

    public OrderMenuRepository(final JdbcTemplateOrderMenuDao jdbcTemplateOrderMenuDao,
                               final JdbcTemplateOrderProductDao jdbcTemplateOrderProductDao) {
        this.jdbcTemplateOrderMenuDao = jdbcTemplateOrderMenuDao;
        this.jdbcTemplateOrderProductDao = jdbcTemplateOrderProductDao;
    }

    @Override
    public OrderMenus save(final OrderMenus entity) {
        final OrderMenus orderMenus = jdbcTemplateOrderMenuDao.saveAll(entity);
        for (final OrderMenu orderMenu : entity.getOrderMenus()) {
            final OrderProducts orderProducts = orderMenu.getOrderProducts();
            final Long orderMenuId = orderMenus.findIdByMenuId(orderMenu.getMenuId());
            orderProducts.setOrderMenuId(orderMenuId);
            final OrderProducts savedOrderProducts = jdbcTemplateOrderProductDao.saveAll(orderProducts);
            orderMenus.setOrderProducts(savedOrderProducts);
        }
        return orderMenus;
    }
}
