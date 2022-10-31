package kitchenpos.repository;

import kitchenpos.domain.order.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long>, TableGroupRepository {
}
