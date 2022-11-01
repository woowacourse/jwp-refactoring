package kitchenpos.order.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.OrderTable;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}
