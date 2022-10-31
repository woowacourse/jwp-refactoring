package kitchenpos.repository;

import kitchenpos.domain.Order;

public interface OrderEntityRepository {
    Order getById(Long id);
}
