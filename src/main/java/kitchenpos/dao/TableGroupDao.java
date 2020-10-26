package kitchenpos.dao;

import kitchenpos.domain.order.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
}
