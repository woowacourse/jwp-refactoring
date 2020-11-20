package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<OrderTable, Long> {
}
