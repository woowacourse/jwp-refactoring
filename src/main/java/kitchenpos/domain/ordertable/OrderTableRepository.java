package kitchenpos.domain.ordertable;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    @Query("select ot from"
            + " OrderTable ot"
            + " left join fetch ot.tableGroup")
    List<OrderTable> findAll();
}
