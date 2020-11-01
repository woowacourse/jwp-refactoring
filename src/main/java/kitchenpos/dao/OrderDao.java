package kitchenpos.dao;

import kitchenpos.domain.Orderz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Orderz, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> asList);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> asList);
}
