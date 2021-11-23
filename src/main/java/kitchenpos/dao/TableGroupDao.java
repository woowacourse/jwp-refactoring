package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.TableGroup;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {

}
