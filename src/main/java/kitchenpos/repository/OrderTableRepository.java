package kitchenpos.repository;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableDao {

    @Override
    default OrderTable save(OrderTable entity) {
        return null;
    }

    @Override
    Optional<OrderTable> findById(Long id);

    @Override
    List<OrderTable> findAll();

    @Override
    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Override
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
