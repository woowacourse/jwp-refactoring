package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableServiceTest extends ServiceTestConfig {

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableRepository);
    }

    @DisplayName("주문 테이블 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable orderTableInput = new OrderTable();
            orderTableInput.setNumberOfGuests(1);
            orderTableInput.setEmpty(true);

            // when
            final OrderTable actual = tableService.create(orderTableInput);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getNumberOfGuests()).isEqualTo(orderTableInput.getNumberOfGuests());
                softly.assertThat(actual.isEmpty()).isEqualTo(orderTableInput.isEmpty());
            });
        }
    }

    @DisplayName("주문 테이블 전체 조회")
    @Nested
    class ReadAll {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable savedOrderTable = saveOccupiedOrderTable();

            // when
            final List<OrderTable> actual = tableService.list();

            // then
            // FIXME: equals&hashcode 적용
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
//                softly.assertThat(actual).containsExactly(savedProduct);
            });
        }
    }

    @DisplayName("주문 테이블 주문 가능 여부 변경")
    @Nested
    class ChangeEmpty {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable savedOrderTable = saveOccupiedOrderTable();
            final OrderTable changing = new OrderTable();
            changing.setEmpty(false);

            // when
            final OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), changing);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isEqualTo(savedOrderTable.getId());
                softly.assertThat(actual.isEmpty()).isEqualTo(changing.isEmpty());
            });
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void fail_if_invalid_orderTable_id() {
            // given
            final OrderTable changing = new OrderTable();
            changing.setEmpty(false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(-111L, changing))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블에 TableGroup 이 존재하면 실패한다.")
        @Test
        void fail_if_tableGroup_is_exist() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            saveTableGroup(List.of(orderTable1, orderTable2));

            final OrderTable changing = new OrderTable();
            changing.setEmpty(false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), changing))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문이 들어간 상태이며 주문 상태가 COOKING 인 경우라면 실패한다.")
        @Test
        void fail_if_orderStatus_is_not_COMPLETION() {
            // given
            final OrderTable savedOrderTable = saveOccupiedOrderTable();
            saveOrder(savedOrderTable);
            final OrderTable changing = new OrderTable();
            changing.setEmpty(false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), changing))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 테이블 방문 손님 수 변경")
    @Nested
    class ChangeNumberOfGuests {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable savedOrderTable = saveOccupiedOrderTable();
            final OrderTable changing = new OrderTable();
            changing.setNumberOfGuests(4);

            // when
            final OrderTable actual = tableService.changeNumberOfGuests(savedOrderTable.getId(), changing);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isEqualTo(savedOrderTable.getId());
                softly.assertThat(actual.getTableGroup()).isEqualTo(changing.getTableGroup());
            });
        }

        @DisplayName("설정하려는 고객 수가 0명 미만인 경우에 실패한다.")
        @Test
        void fail_if_numberOfGuests_are_under_zero() {
            // given
            final OrderTable savedOrderTable = saveOccupiedOrderTable();
            final OrderTable changing = new OrderTable();
            changing.setNumberOfGuests(-1);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changing))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void fail_if_invalid_orderTable_id() {
            // given
            final OrderTable changing = new OrderTable();
            changing.setNumberOfGuests(4);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-111L, changing))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 등록이 불가능한 상태면 실패한다.")
        @Test
        void fail_if_orderTable_is_empty() {
            // given
            final OrderTable orderTable = saveOccupiedOrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            final OrderTable changing = new OrderTable();
            changing.setNumberOfGuests(4);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changing))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
