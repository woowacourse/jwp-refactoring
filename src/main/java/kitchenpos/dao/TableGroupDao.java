package kitchenpos.dao;

import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
}
