package kitchenpos.repository;

import kitchenpos.domain.TableGroup;

public interface TableGroupEntityRepository {
    TableGroup getById(Long id);
}
