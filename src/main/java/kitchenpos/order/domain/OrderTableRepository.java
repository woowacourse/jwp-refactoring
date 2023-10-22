package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface OrderTableRepository extends CrudRepository<OrderTable, Long> {

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

}
