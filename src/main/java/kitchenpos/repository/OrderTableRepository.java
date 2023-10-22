package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableDao {

    @Override
    OrderTable save(OrderTable entity);

    @Override
    Optional<OrderTable> findById(Long id);

    @Override
    List<OrderTable> findAll();

    @Override
    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Override
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
