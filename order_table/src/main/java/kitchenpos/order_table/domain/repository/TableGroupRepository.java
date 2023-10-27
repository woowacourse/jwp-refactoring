package kitchenpos.order_table.domain.repository;

import kitchenpos.order_table.domain.OrderTableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<OrderTableGroup, Long> {

    default OrderTableGroup getById(final Long tableGroupId) {
        return findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("id가 " + tableGroupId + "인 TableGroup을 찾을 수 없습니다!"));
    }

}
