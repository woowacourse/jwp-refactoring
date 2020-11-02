package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.table.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {

    List<Table> findAllByIdIn(List<Long> ids);

    List<Table> findAllByTableGroup_Id(Long tableGroupId);
}
