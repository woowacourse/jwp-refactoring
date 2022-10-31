package kitchenpos.dao.jpa;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupDao extends TableGroupDao, JpaRepository<TableGroup, Long> {
}
