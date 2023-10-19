package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeGuestNumberRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

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
            final OrderTableCreateRequest request = new OrderTableCreateRequest(1, true);

            // when
            final OrderTableResponse actual = tableService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
                softly.assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
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
            final List<OrderTableResponse> actual = tableService.list();

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(savedOrderTable.getId());
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
            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // when
            final OrderTableResponse actual = tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isEqualTo(savedOrderTable.getId());
                softly.assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
            });
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void fail_if_invalid_orderTable_id() {
            // given
            final Long invalidOrderTableId = -111L;
            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, request))
                    .isInstanceOfAny(IllegalArgumentException.class, InvalidDataAccessApiUsageException.class);
        }

        @DisplayName("주문 테이블에 TableGroup 이 존재하면 실패한다.")
        @Test
        void fail_if_tableGroup_is_exist() {
            // given
            final OrderTable orderTable1 = saveEmptyOrderTable();
            final OrderTable orderTable2 = saveEmptyOrderTable();
            saveTableGroup(List.of(orderTable1, orderTable2));

            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 COOKING 인 경우라면 실패한다.")
        @Test
        void fail_if_orderStatus_is_not_COMPLETION() {
            // given
            final OrderTable savedOrderTable = saveOccupiedOrderTable();
            saveOrder(savedOrderTable);
            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
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
            final OrderTableChangeGuestNumberRequest request = new OrderTableChangeGuestNumberRequest(4);

            // when
            final OrderTableResponse actual = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isEqualTo(savedOrderTable.getId());
                softly.assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            });
        }

        @DisplayName("설정하려는 고객 수가 0명 미만인 경우에 실패한다.")
        @Test
        void fail_if_numberOfGuests_are_under_zero() {
            // given
            final OrderTable savedOrderTable = saveOccupiedOrderTable();
            final OrderTableChangeGuestNumberRequest request = new OrderTableChangeGuestNumberRequest(-1);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void fail_if_invalid_orderTable_id() {
            // given
            final Long invalidOrderTableId = -111L;
            final OrderTableChangeGuestNumberRequest request = new OrderTableChangeGuestNumberRequest(4);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, request))
                    .isInstanceOfAny(IllegalArgumentException.class, InvalidDataAccessApiUsageException.class);
        }

        @DisplayName("주문 등록이 불가능한 상태면 실패한다.")
        @Test
        void fail_if_orderTable_is_empty() {
            // given
            final OrderTable emptyOrderTable = saveEmptyOrderTable();
            final OrderTableChangeGuestNumberRequest request = new OrderTableChangeGuestNumberRequest(4);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyOrderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
