package kitchenpos.order.dao;

import kitchenpos.order.domain.OrderProducts;

public interface OrderProductDao {

    OrderProducts saveAll(OrderProducts orderProducts);
}
