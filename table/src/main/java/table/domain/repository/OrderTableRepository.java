package table.domain.repository;

import java.util.List;
import table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIsIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
