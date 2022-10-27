package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableGroupRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.repository.OrderTableRepository;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    private static final long NOT_EXIST_ID = 999L;
    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableService.create(new OrderTableRequest(1, false));
        orderTable2 = orderTableService.create(new OrderTableRequest(1, false));
    }

    @Test
    @DisplayName("orderTable을 생성한다.")
    void create() {
        //given, when
        TableGroup tableGroup = tableGroupService.create(
            new OrderTableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId())));

        //then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables().get(0)).isEqualTo(orderTable1),
            () -> assertThat(tableGroup.getOrderTables().get(1)).isEqualTo(orderTable2)
        );
    }

    @Test
    @DisplayName("존재하지 않는 테이블이 포함된 경우 예외를 발생시킨다.")
    void createNotExistTableError() {
        assertThatThrownBy(() -> tableGroupService.create(
            new OrderTableGroupRequest(Arrays.asList(orderTable1.getId(), NOT_EXIST_ID))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블을 분리시킨다.")
    void ungroup() {
        //given
        TableGroup tableGroup = tableGroupService.create(
            new OrderTableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId())));

        //when
        tableGroupService.ungroup(tableGroup.getId());
        OrderTable orderTable1 = orderTableRepository.findById(this.orderTable1.getId()).get();
        OrderTable orderTable2 = orderTableRepository.findById(this.orderTable2.getId()).get();

        //then
        assertAll(
            () -> assertThat(orderTable1.isEmpty()).isFalse(),
            () -> assertThat(orderTable2.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("COOKING이나 MEAL 상태인 주문이 포함된 테이블을 분리시킬 경우 예외를 발생시킨다.")
    void ungroupWithInvalidStatusError() {
        //given
        Long savedTableGroupId = tableGroupService.create(
                new OrderTableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
            .getId();

        orderService.create(new OrderRequest(orderTable1.getId(), Arrays.asList(new OrderLineItemRequest(1L, 1))));

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
