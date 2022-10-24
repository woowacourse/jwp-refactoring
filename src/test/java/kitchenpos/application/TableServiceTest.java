package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        this.orderTable1 = tableService.create(new OrderTable(10, true));
        this.orderTable2 = tableService.create(new OrderTable(15, true));
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문 테이블을 생성한다.")
        @Test
        void Should_CreateOrderTable() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when
            OrderTable actual = tableService.create(orderTable);

            // then
            assertAll(() -> {
                assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
                assertThat(actual.isEmpty()).isEqualTo(orderTable.isEmpty());
            });
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("생성된 모든 주문 테이블 목록을 조회한다.")
        @Test
        void Should_ReturnAllOrderTableList() {
            // given
            tableService.create(new OrderTable(1, false));
            tableService.create(new OrderTable(2, false));
            tableService.create(new OrderTable(3, false));

            // when
            List<OrderTable> actual = tableService.list();

            // then
            assertThat(actual).hasSize(5);
        }
    }

    @DisplayName("changeEmpty 메소드는")
    @Nested
    class ChangeEmptyMethod {

        @DisplayName("빈 테이블 여부를 변경한다.")
        @Test
        void Should_ChangeIsEmptyTable() {
            // given & when
            OrderTable actual = tableService.changeEmpty(orderTable1.getId(), new OrderTable(true));

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("주문 테이블이 존재하지 않으면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given & when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable2.getId() + 1, new OrderTable(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 단체 지정 정보가 존재한다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableHasTableGroup() {
            // given
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
            tableGroupService.create(tableGroup);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), new OrderTable(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("changeNumberOfGuests 메소드는")
    @Nested
    class ChangeNumberOfGuestsMethod {

        @DisplayName("주문 테이블의 방문한 손님 수를 변경한다.")
        @Test
        void Should_ChangeNumberOfGuests() {
            // given
            OrderTable oldOrderTable = tableService.create(new OrderTable(1, false));
            OrderTable newOrderTable = new OrderTable(100);

            // when
            OrderTable actual = tableService.changeNumberOfGuests(oldOrderTable.getId(), newOrderTable);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(newOrderTable.getNumberOfGuests());
        }

        @DisplayName("방문한 손님 수가 0보다 작다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_NumberOfGuestsIsLessThan0() {
            // given
            OrderTable newOrderTable = new OrderTable(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("전달된 주문 테이블 ID에 대한 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            OrderTable oldOrderTable = tableService.create(new OrderTable(3, false));
            OrderTable newOrderTable = new OrderTable(100);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(oldOrderTable.getId() + 1, newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("전달된 주문 테이블 ID에 대한 주문 테이블이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTable oldOrderTable = tableService.create(new OrderTable(3, true));
            OrderTable newOrderTable = new OrderTable(100);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(oldOrderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
