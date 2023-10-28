package kitchenpos.domain.table;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("select o from OrderTable o where o.tableGroupId = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    default OrderTable getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테이블입니다."));
    }
}
