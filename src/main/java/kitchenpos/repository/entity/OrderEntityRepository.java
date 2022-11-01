package kitchenpos.repository.entity;

import kitchenpos.domain.Order;

public interface OrderEntityRepository {
    Order getById(Long id);
}
