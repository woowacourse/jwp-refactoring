package kitchenpos.ordertable.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findByTableGroupId(Long tableGroupId);
}
