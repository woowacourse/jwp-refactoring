package kitchenpos.table.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.table.domain.TableGroup;

public interface TableGroupRepository extends Repository<TableGroup, Long> {
    TableGroup save(TableGroup tableGroup);

    Optional<TableGroup> findById(Long tableGroupId);
}
