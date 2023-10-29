package kitchenpos.ordertable.persistence;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {

    private final JpaOrderTableRepository jpaOrderTableRepository;

    public OrderTableRepositoryImpl(final JpaOrderTableRepository jpaOrderTableRepository) {
        this.jpaOrderTableRepository = jpaOrderTableRepository;
    }

    @Override
    public OrderTable save(final OrderTable orderTable) {
        return jpaOrderTableRepository.save(orderTable);
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return jpaOrderTableRepository.findById(id);
    }

    @Override
    public List<OrderTable> findAll() {
        return jpaOrderTableRepository.findAll();
    }

    @Override
    public List<OrderTable> findAllByIdsIn(final List<Long> ids) {
        return jpaOrderTableRepository.findAllByIdIn(ids);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return jpaOrderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    @Override
    public List<OrderTable> saveAll(final List<OrderTable> orderTables) {
        return jpaOrderTableRepository.saveAll(orderTables);
    }
}
