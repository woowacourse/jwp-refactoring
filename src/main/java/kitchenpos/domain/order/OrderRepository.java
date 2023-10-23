package kitchenpos.domain.order;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.orderTable.id = :orderTableId")
    Optional<Order> findByOrderTableId(Long orderTableId);

    @Query("select o from Order o where o.orderTable.id in :orderTableIds")
    List<Order> findAllByOrderTableIds(List<Long> orderTableIds);

    default Order getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다."));
    }
}
