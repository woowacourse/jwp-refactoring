package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupDao extends JpaRepository<TableGroup, Long>, TableGroupDao {
}
