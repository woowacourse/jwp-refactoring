package kitchenpos.application;

import kitchenpos.application.test.ServiceUnitTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.fixture.OrderTableFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TableServiceTest extends ServiceUnitTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        OrderTable 주문_테이블 = 주문_테이블_생성();
        this.orderTable = 주문_테이블;
    }

    @Nested
    class 주문_테이블을_저장한다 {

        @Test
        void 주문_테이블을_저장한다() {
            // when, then
            assertDoesNotThrow(
                    () -> tableService.create(orderTable)
            );
        }

    }

    @Nested
    class 주문_목록을_반환한다 {

        @Test
        void 주문_목록을_반환한다() {
            // given
            when(orderTableDao.findAll()).thenReturn(List.of(orderTable));

            // when
            List<OrderTable> orderTables = tableService.list();

            // then
            assertAll(
                    () -> assertThat(orderTables).hasSize(1),
                    () -> assertThat(orderTables.get(0)).isEqualTo(orderTable)
            );
        }

    }

    @Nested
    class 주문_목록을_empty로_변경한다 {

        @Test
        void 주문_목록을_empty로_변경한다() {
            // given
            when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                    .thenReturn(false);
            when(orderTableDao.save(orderTable)).thenReturn(orderTable);

            // when
            OrderTable savedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

            // then
            assertAll(
                    () -> assertThat(savedOrderTable).isEqualTo(savedOrderTable),
                    () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty())
            );
        }

        @Test
        void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
            // given
            when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(null));

            // when
            assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(orderTable.getId(), orderTable));
        }

        @Test
        void 주문_테이블의_tableGroupId가_존재한다면_예외가_발생한다() {
            // given
            orderTable.setTableGroupId(1L);
            when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));

            // when
            assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(orderTable.getId(), orderTable));
        }

        @Test
        void 주문_상태가_COOKING_또는_MEAL이라면_예외가_발생한다() {
            // given
            when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
            when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
            when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                    .thenReturn(true);

            // when
            assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(orderTable.getId(), orderTable));
        }

    }

}