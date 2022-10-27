package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableGroupRequest;
import kitchenpos.dto.OrderTableRequest;

@SpringBootTest
@Transactional
class OrderTableServiceTest {

    private static final long NOT_EXIST_ID = 9999L;

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("존재하지 않는 테이블을 빈 테이블로 수정하려고 하면 예외를 발생시킨다.")
    void changeEmptyNotExistTableError() {
        assertThatThrownBy(() -> orderTableService.changeEmpty(NOT_EXIST_ID, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Group으로 묶인 테이블의 경우 빈 테이블로 수정하려고 하면 예외를 발생시킨다.")
    void changeEmptyWithGroupTableError() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);

        Long orderTableId1 = orderTableService.create(orderTableRequest).getId();
        Long orderTableId2 = orderTableService.create(orderTableRequest).getId();

        tableGroupService.create(new OrderTableGroupRequest(Arrays.asList(orderTableId1, orderTableId2)));

        //then
        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTableId1, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 상태가 COOKING 또는 MEAL일 때 빈 테이블로 변경하려고 할 경우 예외를 발생시킨다.")
    void changeEmptyInvalidStatusError() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);

        Long orderTableId1 = orderTableService.create(orderTableRequest).getId();

        OrderRequest orderRequest = new OrderRequest(orderTableId1, Arrays.asList(new OrderLineItemRequest(1L, 1)));
        orderService.create(orderRequest);

        //then
        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTableId1, true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객의 수를 수정한다.")
    void changeNumberOfGuests() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);

        Long orderTableId1 = orderTableService.create(orderTableRequest).getId();

        //when
        OrderTable actual = orderTableService.changeNumberOfGuests(orderTableId1, 5);

        //then
        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("존재하지 않는 테이블의 고객 수를 수정하려고 할 경우 예외를 발생시킨다.")
    void changeNumberOfGuestsNotExistTableError() {
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(NOT_EXIST_ID, 10))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
