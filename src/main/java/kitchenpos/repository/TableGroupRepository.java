package kitchenpos.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.TableGroup;

public interface TableGroupRepository extends Repository<TableGroup, Long> {
    TableGroup save(TableGroup tableGroup);

    Optional<TableGroup> findById(Long tableGroupId);
}
