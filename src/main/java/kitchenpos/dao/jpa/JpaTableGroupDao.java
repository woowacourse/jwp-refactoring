package kitchenpos.dao.jpa;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaTableGroupDao extends TableGroupDao, JpaRepository<TableGroup, Long> {
}
