package kitchenpos.application;

import kitchenpos.order.application.dto.OrderTableId;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.ordertablegroup.application.OrderTableGroupService;
import kitchenpos.ordertablegroup.application.dto.OrderTableGroupCreateRequest;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.repository.OrderTableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class OrderTableGroupServiceTest {
    @Autowired
    private OrderTableGroupService orderTableGroupService;
    @Autowired
    private OrderTableGroupRepository orderTableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("생성에 성공한다.")
    void create() {
        // given
        final OrderTable orderTable1 = OrderTable.of(5, true);
        final OrderTable orderTable2 = OrderTable.of(5, true);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        final OrderTableGroupCreateRequest request = new OrderTableGroupCreateRequest(
                Arrays.asList(new OrderTableId(orderTable1.getId()), new OrderTableId(orderTable2.getId()))
        );

        // when
        final Long tableGroupId = orderTableGroupService.create(request);
        final OrderTableGroup orderTableGroup = orderTableGroupRepository.findById(tableGroupId).get();

        // then
        Assertions.assertThat(orderTableGroup.getOrderTables().getOrderTables()).hasSize(2);
    }

    @Test
    @DisplayName("해제에 성공한다.")
    void ungroup() {
        // given
        final OrderTable orderTable1 = OrderTable.of(5, true);
        final OrderTable orderTable2 = OrderTable.of(5, true);
        final OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);

        final OrderTableGroup orderTableGroup = OrderTableGroup.of(LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        final OrderTableGroup savedOrderTableGroup = orderTableGroupRepository.save(orderTableGroup);

        // when
        orderTableGroupService.ungroup(savedOrderTableGroup.getId());
        final OrderTableGroup updatedOrderTableGroup = orderTableGroupRepository.findById(savedOrderTableGroup.getId()).get();

        // then
        for (OrderTable orderTable : updatedOrderTableGroup.getOrderTables().getOrderTables()) {
            Assertions.assertThat(orderTable.getTableGroupId()).isNull();
            Assertions.assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @Test
    @DisplayName("주문 테이블 검증 실패 - 주문 테이블의 수와 요청 ID의 수가 일치하지 않는 경우")
    void validateFailWhenTableSizeDoesNotMatchRequestIds() {
        // given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(4, true);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        OrderTableGroupCreateRequest request = new OrderTableGroupCreateRequest(
                List.of(new OrderTableId(orderTable1.getId()), new OrderTableId(999L)));

        // when
        // then
        assertThatThrownBy(
                () -> orderTableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
