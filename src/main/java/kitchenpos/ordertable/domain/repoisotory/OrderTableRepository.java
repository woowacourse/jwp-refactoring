package kitchenpos.ordertable.domain.repoisotory;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> orderTableIdValues);

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
