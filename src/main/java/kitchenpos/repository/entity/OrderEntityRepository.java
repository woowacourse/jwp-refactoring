package kitchenpos.repository.entity;

import kitchenpos.domain.order.Order;

public interface OrderEntityRepository {
    Order getById(Long id);
}
