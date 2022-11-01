package kitchenpos.repository.entity;

import kitchenpos.domain.TableGroup;

public interface TableGroupEntityRepository {
    TableGroup getById(Long id);
}
