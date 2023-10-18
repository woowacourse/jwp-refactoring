package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAll();

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
