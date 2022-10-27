package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.jpa.repository.JpaOrderTableRepository;
import kitchenpos.domain.OrderTable;

@Primary
@Repository
public class JpaOrderTableDao implements OrderTableDao {

    private final JpaOrderTableRepository orderTableRepository;

    public JpaOrderTableDao(final JpaOrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        return orderTableRepository.save(entity);
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        return orderTableRepository.findById(id);
    }

    @Override
    public List<OrderTable> findAll() {
        return orderTableRepository.findAll();
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return orderTableRepository.findAllById(ids);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
