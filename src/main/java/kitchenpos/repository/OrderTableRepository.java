package kitchenpos.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.entity.OrderTableEntityRepository;
import org.springframework.data.repository.Repository;

public interface OrderTableRepository extends Repository<OrderTable, Long>, OrderTableEntityRepository {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(Collection<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
