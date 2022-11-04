package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.table.application.UngroupValidator;
import org.springframework.stereotype.Component;

@Component
public class UngroupValidatorImp implements UngroupValidator {

    private final OrderDao orderDao;

    public UngroupValidatorImp(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void validateUngroup(List<Long> orderTableIds) {
        List<Order> orders = orderDao.findAll();
        for (Order order : orders) {
            order.validateUngroup(orderTableIds);
        }
    }
}
