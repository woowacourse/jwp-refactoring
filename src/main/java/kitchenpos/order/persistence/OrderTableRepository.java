package kitchenpos.order.persistence;


import java.util.List;
import kitchenpos.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);
}
