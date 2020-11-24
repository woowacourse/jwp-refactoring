package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    @Override
    List<TableGroup> findAll();
}
