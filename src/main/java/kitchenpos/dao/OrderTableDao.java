package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroup(TableGroup tableGroupId);

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

//    OrderTable save(OrderTable entity);
//
//    Optional<OrderTable> findById(Long id);
//
//    List<OrderTable> findAll();
//
//    List<OrderTable> findAllByIdIn(List<Long> ids);
//
//    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
