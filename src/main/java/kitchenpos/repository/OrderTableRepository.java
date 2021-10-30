package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);

    @Query("select distinct orderTable " +
            "from OrderTable as orderTable " +
            "left join fetch orderTable.tableGroup ")
    List<OrderTable> findAllFetchJoinTableGroup();
}
