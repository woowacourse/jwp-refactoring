package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderTableDao {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByOrderTableGroupId(Long orderTableGroupId);
}
