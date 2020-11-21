package kitchenpos.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findByTableGroupId(Long tableGroupId);
}
