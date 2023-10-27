package kitchenpos.domain;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    default Order getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("id가 %d인 주문을 찾을 수 없습니다.".formatted(id)));
    }

    boolean existsByOrderTableIdAndOrderStatusIn(Long id, List<String> name);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> list);
}
