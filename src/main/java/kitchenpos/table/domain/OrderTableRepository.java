package kitchenpos.table.domain;

import java.util.List;

public interface OrderTableRepository {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    Long findTableGroupIdById(Long id);
}
