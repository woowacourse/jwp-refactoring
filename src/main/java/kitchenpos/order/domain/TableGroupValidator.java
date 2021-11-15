package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateMapping(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("두 테이블 이상이어야 그룹으로 묶을 수 있습니다.");
        }

        orderTables.forEach(it -> {
            if (!it.isEmpty()) {
                throw new IllegalArgumentException("비어있지 않은 상태에선 그룹을 적용할 수 없습니다.");
            }
            if (Objects.nonNull(it.getTableGroupId())) {
                throw new IllegalArgumentException("이미 그룹이 있는 상태에선 그룹을 적용할 수 없습니다");
            }
        });
    }

    public void validateUnmapping(List<OrderTable> orderTables){
        orderTables.forEach(it->{
            if (Objects.isNull(it.getTableGroupId())) {
                throw new IllegalArgumentException("그룹 정보가 존재하지 않는 테이블입니다.");
            }
        });

        List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 중, 식사 중에 있는 주문 테이블의 그룹을 해제할 수 없습니다.");
        }
    }
}
