package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public static void validateGroup(OrderTable orderTable, Long tableGroupId) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 상태에선 그룹을 적용할 수 없습니다.");
        }
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 그룹이 있는 상태에선 그룹을 적용할 수 없습니다");
        }
        if (Objects.isNull(tableGroupId)) {
            throw new IllegalArgumentException("적절하지 않은 그룹 정보입니다.");
        }
    }

    public void validateChangeEmpty(OrderTable orderTable, boolean empty) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 중, 또는 식사 중에 있는 테이블의 Empty 여부를 수정할 수 없습니다.");
        }

        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("테이블 그룹이 있는 상태에선 Empty 여부를 수정할 수 없습니다.");
        }
    }

    public static void validateChangeNumberOfGuests(OrderTable orderTable, int numberOfGuests) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 상태에선 손님 수를 변경할 수 없습니다.");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("유효하지 않은 손님 수입니다.");
        }
    }

    public static void validateUngroup(OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("그룹 정보가 존재하지 않는 테이블입니다.");
        }
    }
}
