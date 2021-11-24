package kitchenpos.repository;

import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
