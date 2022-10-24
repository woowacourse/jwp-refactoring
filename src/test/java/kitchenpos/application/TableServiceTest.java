package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        final OrderTable orderTable = new OrderTable();
        final OrderTable actual = tableService.create(orderTable);

        assertAll(
                () -> assertThat(actual.isEmpty()).isTrue(),
                () -> assertThat(actual.getNumberOfGuests()).isZero()
        );
    }

    @Test
    @DisplayName("테이블 목록을 반환한다.")
    void list() {
        final List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(8);
    }

    @Nested
    @DisplayName("테이블 상태 변환 테스트")
    class changeEmpty{

        @Test
        @DisplayName("테이블 상태를 empty를 true로 바꿔준다.")
        void changeEmpty(){
            final OrderTable orderTable = new OrderTable(null, 2, true);
            final Long savedId = 1L;
            orderTable.setId(savedId);

            final OrderTable actual = tableService.changeEmpty(savedId, orderTable);

            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 테이블의 상태를 반환하려 시도하면 예외를 발생시킨다.")
        void changeEmpty_notExistTable(){
            final OrderTable orderTable = new OrderTable(1L, 2, true);
            final Long notExistOrderTableId = 9999999L;

            assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, orderTable))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹테이블이 지정되어있으면 예외를 발생시킨다.")
        void changeEmpty_existTableGroupId(){
            final OrderTable orderTable1 = new OrderTable(null, 2, true);
            final OrderTable orderTable2 = new OrderTable(null, 2, true);
            orderTable1.setId(1L);
            orderTable2.setId(2L);
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            final Long tableGroupId = tableGroupService.create(tableGroup).getId();
            final OrderTable orderTable = new OrderTable(tableGroupId, 2, true);
            final Long savedId = 1L;
            orderTable.setId(savedId);

            assertThatThrownBy(() -> tableService.changeEmpty(savedId, orderTable))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("테이블 상태가 조리중이거나 식사중이면 예외를 발생시킨다.")
        void changeEmpty_cookingOrMeal(final String orderStatus){
            final OrderTable newOrderTable = new OrderTable(null, 2, false);
            final Long orderTableId = tableService.create(newOrderTable).getId();
            final Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderStatus(orderStatus);
            order.setOrderLineItems(List.of(new OrderLineItem(1L, 2L)));
            orderService.create(order);

            final OrderTable orderTable = new OrderTable(null, 2, true);
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                            .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("손님 수 변경 테스트.")
    class changeNumberOfGuests{

        @Test
        @DisplayName("테이블 손님 수를 변경한다.")
        void changeNumberOfGuests(){
            final OrderTable newOrderTable = new OrderTable(null, 2, false);
            final Long orderTableId = tableService.create(newOrderTable).getId();

            final OrderTable orderTable = new OrderTable(null, 3, false);
            final OrderTable actual = tableService.changeNumberOfGuests(orderTableId, orderTable);

            assertThat(actual.getNumberOfGuests()).isEqualTo(3);
        }

        @Test
        @DisplayName("변경하려는 테이블 손님 수가 음수인 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_negativeGuest(){
            final OrderTable newOrderTable = new OrderTable(1L, 2, false);
            final Long orderTableId = tableService.create(newOrderTable).getId();

            final OrderTable orderTable = new OrderTable(1L, -3, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 테이블의 손님 수를 변경하려 하는 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_notExistOrderTable(){
            final Long notExistTableId = 999999L;

            final OrderTable orderTable = new OrderTable(null, -3, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTableId, orderTable))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블이 비어있는데 손님 수를 변경하려는 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_EmptyTable(){
            final OrderTable newOrderTable = new OrderTable(null, 0, true);
            final Long orderTableId = tableService.create(newOrderTable).getId();

            final OrderTable orderTable = new OrderTable(null, -3, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                            .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
