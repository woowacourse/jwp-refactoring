package kitchenpos.table.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;

public interface OrderTableDao {

    Long save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    void updateTableGroupIdAndEmpty(Long id, Long tableGroupId, boolean empty);

    void updateNumberOfGuests(Long id, int numberOfGuests);

    void updateEmpty(Long id, boolean empty);
}
