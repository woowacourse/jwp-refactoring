package kitchenpos.domain.table;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends Repository<OrderTable, Long> {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("select o from OrderTable o where o.tableGroupId = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
