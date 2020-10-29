package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.TableGroup;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
