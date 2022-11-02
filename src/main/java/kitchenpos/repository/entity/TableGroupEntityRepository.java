package kitchenpos.repository.entity;

import kitchenpos.domain.tablegroup.TableGroup;

public interface TableGroupEntityRepository {
    TableGroup getById(Long id);
}
