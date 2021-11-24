package kitchenpos.Order.domain.repository;

import kitchenpos.Order.domain.Order;
import kitchenpos.OrderTable.domain.OrderTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatus);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatus);

    @EntityGraph(attributePaths = {"orderLineItems"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"orderLineItems"})
    Optional<Order> findById(Long id);
}
