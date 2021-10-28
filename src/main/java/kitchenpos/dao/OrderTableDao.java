package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
    OrderTable save(OrderTable entity);

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
