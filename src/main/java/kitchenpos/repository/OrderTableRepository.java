package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    @Override
    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
