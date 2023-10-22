package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문 테이블이 존재하지 않습니다."));
    }

    default OrderTables getAllById(final List<Long> orderTables) {
        final List<OrderTable> results = findAllById(orderTables);

        if (results.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 포함되어 있습니다.");
        }

        return new OrderTables(results);
    }

    List<OrderTable> findByTableGroup(final TableGroup tableGroup);

    default OrderTables getByTableGroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = findByTableGroup(tableGroup);

        return new OrderTables(orderTables);
    }
}
