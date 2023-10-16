package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private OrderTable emptyTable;

    private OrderTable notEmptyTable;

    @BeforeEach
    void setup() {
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(false);
        notEmptyTable = orderTableDao.save(orderTable2);

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);
        emptyTable = orderTableDao.save(orderTable1);
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void createOrderTable() {
        // given
        final OrderTable orderTableRequest = new OrderTable();
        orderTableRequest.setNumberOfGuests(0);
        orderTableRequest.setEmpty(true);

        // when
        final OrderTable createdTable = orderTableDao.save(orderTableRequest);

        // then
        assertThat(createdTable.getId()).isNotNull();
    }

    @Test
    @DisplayName("생성된 테이블 정보들을 가져온다.")
    void getTableList() {
        // given

        // when
        final List<OrderTable> tables = tableService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(tables).isNotEmpty();
            final OrderTable createdTable = tables.get(tables.size() - 1);
            softly.assertThat(createdTable.getId()).isEqualTo(emptyTable.getId());
        });
    }

    @Nested
    @DisplayName("테이블 상태 변경 테스트")
    class ChangeEmptyStatusTest {

        @Test
        @DisplayName("상태변경에 성공한다. - empty true -> false")
        void successFromTrueToFalse() {
            // given
            final OrderTable changeRequest = new OrderTable();
            changeRequest.setEmpty(false);

            // when
            final OrderTable response = tableService.changeEmpty(emptyTable.getId(), changeRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(emptyTable.getId());
                softly.assertThat(response.isEmpty()).isFalse();
            });
        }

        @Test
        @DisplayName("상태변경에 성공한다. - empty true -> false")
        void successFromFalseToTrue() {
            // given
            final OrderTable changeRequest = new OrderTable();
            changeRequest.setEmpty(true);

            // when
            final OrderTable response = tableService.changeEmpty(notEmptyTable.getId(), changeRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(notEmptyTable.getId());
                softly.assertThat(response.isEmpty()).isTrue();
            });
        }

        @Test
        @DisplayName("테이블 그룹에 속해있으면 상태를 변경할 수 없다.")
        void throwExceptionWithGroupedTable() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(LocalDateTime.now());
            final TableGroup createdTableGroup = tableGroupDao.save(tableGroup);

            notEmptyTable.setId(null);
            notEmptyTable.setTableGroupId(createdTableGroup.getId());
            notEmptyTable = orderTableDao.save(notEmptyTable);

            final OrderTable changeRequest = new OrderTable();
            changeRequest.setEmpty(true);

            // when
            // then
            final Long tableId = notEmptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeEmpty(tableId, changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "테이블의 주문 상테 : {0}")
        @ValueSource(strings = {"MEAL", "COOKING"})
        @DisplayName("완료되지 않은 주문이 있으면 empty상태로 변경할 수 없다.")
        void throwExceptionWithUnCompletedOrder(final String status) {
            // given
            final Order order = getOrderFor(notEmptyTable.getId(), status);
            orderDao.save(order);
            final OrderTable changeRequest = new OrderTable();
            changeRequest.setEmpty(true);

            // when
            // then
            final Long tableId = notEmptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeEmpty(tableId, changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Order getOrderFor(final Long tableId, final String status) {
            final Order order = new Order();
            order.setOrderTableId(tableId);
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderStatus(status);
            return order;
        }
    }

    @Nested
    @DisplayName("테이블의 손님의 수를 바꿀 수 있다.")
    class ChangeGuestNumberTest {

        @Test
        @DisplayName("손님의 수를 변경하는데 성공한다.")
        void success() {
            // given
            final OrderTable orderTableRequest = new OrderTable();
            orderTableRequest.setNumberOfGuests(1);

            // when
            final OrderTable response = tableService.changeNumberOfGuests(notEmptyTable.getId(), orderTableRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(notEmptyTable.getId());
                softly.assertThat(response.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
            });
        }

        @Test
        @DisplayName("변경하려는 손님의 수가 0보다 작은경우 예외가 발생한다.")
        void throwExceptionWithNegativeGuestNumber() {
            // given
            final OrderTable orderTableRequest = new OrderTable();
            orderTableRequest.setNumberOfGuests(-1);

            // when
            // then
            final Long tableId = notEmptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, orderTableRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("empty상태의 테이블에서는 손님의 수를 변경할 경우 예외가 발생한다.")
        void throwExceptionWithEmptyTable() {
            // given
            final OrderTable orderTableRequest = new OrderTable();
            orderTableRequest.setNumberOfGuests(1);

            // when
            // then
            final Long tableId = emptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, orderTableRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
