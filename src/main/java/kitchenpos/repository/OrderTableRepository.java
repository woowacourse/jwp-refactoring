package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.table.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}
