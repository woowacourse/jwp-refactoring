package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
//    OrderTable save(OrderTable entity);
//
//    Optional<OrderTable> findById(Long id);
//
//    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
