package kitchenpos.application;

import static kitchenpos.common.OrderTableFixtures.ORDER_TABLE1_CREATE_REQUEST;
import static kitchenpos.common.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @Test
    @DisplayName("주문 테이블 생성 시 ID와 테이블 그룹 ID를 null로 설정하여 저장한다.")
    void create() {
        // given
        OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);

        BDDMockito.given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(orderTable);

        // when
        final OrderTable createdOrderTable = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(createdOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
            softly.assertThat(createdOrderTable.getTableGroupId()).isNull();
        });
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블 여부 변경 시")
    class ChangeEmpty {

        @Test
        @DisplayName("빈 테이블 여부 변경에 성공한다.")
        void success() {
            // given
            final OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
            orderTable.setId(1L);
            orderTable.setTableGroupId(null);

            BDDMockito.given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(orderTable);

            BDDMockito.given(orderTableDao.findById(any(Long.class)))
                    .willReturn(Optional.of(orderTable));

            final OrderTable createdOrderTable = tableService.create(ORDER_TABLE1_CREATE_REQUEST());
            final Long orderTableId = createdOrderTable.getId();
            final OrderTable orderTableRequest = new OrderTable();
            orderTableRequest.setEmpty(false);

            // when
            OrderTable changedOrderTable = tableService.changeEmpty(orderTableId, orderTableRequest);

            // then
            assertThat(changedOrderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundOrderTable() {
            // given
            final Long notExistOrderTableId = -1L;
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, orderTable))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("주문 테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
        void throws_ExistTableGroup() {
            // given
            long existTableGroupId = 1L;

            OrderTable orderTableRequest = new OrderTable();
            orderTableRequest.setId(1L);
            orderTableRequest.setTableGroupId(existTableGroupId);
            BDDMockito.given(orderTableDao.findById(any(Long.class)))
                    .willReturn(Optional.of(orderTableRequest));


            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                    .isInstanceOf(OrderTableException.AlreadyExistTableGroupException.class)
                    .hasMessage("[ERROR] 이미 Table Group이 존재합니다.");
        }

        @Test
        @DisplayName("주문 테이블 ID에 해당하는 주문이 존재하고 주문 상태가 조리 or 식사면 예외가 발생한다.")
        void throws_existsByOrderTableIdAndOrderStatusIn() {
            // given
            OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
            orderTable.setId(1L);
            orderTable.setTableGroupId(null);
            BDDMockito.given(orderTableDao.findById(any(Long.class)))
                    .willReturn(Optional.of(orderTable));

            final List<String> status = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
            BDDMockito.given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), eq(status)))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                    .isInstanceOf(CannotChangeEmptyStateByOrderStatusException.class)
                    .hasMessage("[ERROR] 주문 테이블의 상태가 조리중이거나 식사중입니다.");
        }
    }

    @Nested
    @DisplayName("주문 테이블의 손님 수 변경 시")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("변경에 성공한다.")
        void success() {
            // given
            final int numberOfGuestsToChange = ORDER_TABLE1_NUMBER_OF_GUESTS + 1;
            OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
            orderTable.setId(1L);
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            BDDMockito.given(orderTableDao.findById(any(Long.class)))
                    .willReturn(Optional.of(orderTable));

            orderTable.setNumberOfGuests(numberOfGuestsToChange);

            BDDMockito.given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(orderTable);

            // when
            OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
            // then
            assertSoftly(softly -> {
                softly.assertThat(changedOrderTable.getNumberOfGuests()).isNotEqualTo(ORDER_TABLE1_NUMBER_OF_GUESTS);
                softly.assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuestsToChange);
            });
        }
        
        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE})
        @DisplayName("손님 수가 0 미만이면 예외가 발생한다.")
        void throws_numberOfGuestsLessThanZero(final int numberOfGuests) {
            // given
            OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
            orderTable.setNumberOfGuests(numberOfGuests);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 손님 수는 0명 이상이어야합니다.");
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_orderTableNotExist() {
            // given
            OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();

            BDDMockito.given(orderTableDao.findById(any(Long.class)))
                    .willThrow(new OrderTableException.NotFoundOrderTableException());

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 비어있는 상태면 예외가 발생한다.")
        void throws_orderTableIsEmpty() {
            // given
            OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
            orderTable.setId(1L);
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(true);
            BDDMockito.given(orderTableDao.findById(any(Long.class)))
                    .willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                    .isInstanceOf(OrderTableException.CannotChangeNumberOfGuestsStateInEmptyException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있는 상태에서 손님 수를 변경할 수 없습니다.");
        }
    }
}
