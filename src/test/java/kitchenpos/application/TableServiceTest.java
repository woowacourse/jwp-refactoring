package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    private OrderTable savedOrderTable1;
    private OrderTable savedOrderTable2;

    @BeforeEach
    void setUp() {
        savedOrderTable1 = saveOrderTable(10, true);
        savedOrderTable2 = saveOrderTable(15, true);
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문 테이블을 생성한다.")
        @Test
        void Should_CreateOrderTable() {
            // given
            OrderTableRequest request = new OrderTableRequest(10, false);

            // when
            OrderTableResponse actual = tableService.create(request);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
                assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
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
            int expected = 4;
            for (int i = 0; i < expected; i++) {
                saveOrderTable(10, true);
            }

            // when
            List<OrderTableResponse> actual = tableService.list();

            // then
            assertThat(actual).hasSize(expected + 2);
        }
    }

    @DisplayName("changeEmpty 메소드는")
    @Nested
    class ChangeEmptyMethod {

        @DisplayName("빈 테이블 여부를 변경한다.")
        @Test
        void Should_ChangeIsEmptyTable() {
            // given
            OrderTableRequest request = new OrderTableRequest(10, false);

            // when
            OrderTableResponse actual = tableService.changeEmpty(savedOrderTable1.getId(), request);

            // then
            assertThat(actual.isEmpty()).isFalse();
        }

        @DisplayName("주문 테이블이 존재하지 않으면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            OrderTableRequest request = new OrderTableRequest(10, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable2.getId() + 1, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 단체 지정 정보가 존재한다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableHasTableGroup() {
            // given
            tableGroupService.create(new TableGroupRequest(
                    List.of(savedOrderTable1.getId(), savedOrderTable2.getId())));

            OrderTableRequest request = new OrderTableRequest(10, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), request))
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
            int expected = 100;
            OrderTable orderTable = saveOrderTable(1, false);
            OrderTableRequest request = new OrderTableRequest(expected, false);

            // when
            OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(expected);
        }

        @DisplayName("방문한 손님 수가 0보다 작다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_NumberOfGuestsIsLessThan0() {
            // given
            OrderTableRequest request = new OrderTableRequest(-1, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable1.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("전달된 주문 테이블 ID에 대한 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            OrderTable orderTable = saveOrderTable(1, false);
            OrderTableRequest request = new OrderTableRequest(100, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId() + 1, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("전달된 주문 테이블 ID에 대한 주문 테이블이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTable orderTable = saveOrderTable(1, true);
            OrderTableRequest request = new OrderTableRequest(100, false);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
