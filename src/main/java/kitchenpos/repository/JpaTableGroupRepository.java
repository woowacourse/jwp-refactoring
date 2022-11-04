package kitchenpos.repository;

import kitchenpos.domain.ordertable.TableGroup;
import org.springframework.data.repository.Repository;

public interface JpaTableGroupRepository extends Repository<TableGroup, Long>, TableGroupRepository {
}
