package kitchenpos.domain.repository;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long>, TableGroupDao {
}
