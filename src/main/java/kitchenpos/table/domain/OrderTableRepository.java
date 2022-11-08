package kitchenpos.table.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface OrderTableRepository extends CrudRepository<OrderTable, Long> {

    default OrderTable getOrderTable(final Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    @Override
    <S extends OrderTable> List<S> saveAll(Iterable<S> entities);
}
