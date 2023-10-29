package kitchenpos.core.ordertable.application;

import java.util.List;
import kitchenpos.core.ordertable.domain.OrderTable;
import org.springframework.data.repository.CrudRepository;

public interface OrderTableDao extends CrudRepository<OrderTable, Long> {
    default OrderTable findMandatoryById(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
