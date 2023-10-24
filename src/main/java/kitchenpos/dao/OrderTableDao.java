package kitchenpos.dao;

import kitchenpos.dto.OrderTableDto;

import java.util.List;
import java.util.Optional;

public interface OrderTableDao {
    OrderTableDto save(OrderTableDto entity);

    Optional<OrderTableDto> findById(Long id);

    List<OrderTableDto> findAll();

    List<OrderTableDto> findAllByIdIn(List<Long> ids);

    List<OrderTableDto> findAllByTableGroupId(Long tableGroupId);
}
