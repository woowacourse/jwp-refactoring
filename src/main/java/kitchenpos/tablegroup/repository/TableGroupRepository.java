package kitchenpos.tablegroup.repository;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @Query("SELECT tg "
            + "FROM TableGroup tg "
            + "JOIN FETCH OrderTable ot")
    List<TableGroup> findAll();
}
