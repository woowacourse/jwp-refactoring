package kitchenpos.table.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    @Query("select t.id from OrderTable t where t.id in ?1")
    List<Long> findIdByIds(List<Long> ids);

    default boolean existsAllByIds(List<Long> orderTableIds) {
        return findIdByIds(orderTableIds).size() == orderTableIds.size();
    }
}
