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

    private JpaOrderTableRepository orderTableRepository;

    public JpaOrderTableDao(JpaOrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public OrderTable save(OrderTable entity) {
        return orderTableRepository.save(entity);
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return orderTableRepository.findById(id);
    }

    @Override
    public List<OrderTable> findAll() {
        return orderTableRepository.findAll();
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findByTableGroupId(tableGroupId);
    }

}
