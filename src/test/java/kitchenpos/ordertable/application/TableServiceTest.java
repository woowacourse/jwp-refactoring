package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.order.apllication.OrderService;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.request.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.response.OrderTableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {
    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void createOrderTable() {
        // given
        final OrderTableCreateRequest request = new OrderTableCreateRequest(1, true);

        // when
        final Long orderTableId = tableService.create(request);

        // then
        assertThat(orderTableId).isPositive();
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void getOrderTables() {
        // given
        final int numberOfGuests = 1;
        final boolean isEmpty = true;
        final OrderTableCreateRequest request = new OrderTableCreateRequest(numberOfGuests, isEmpty);
        final Long orderTableId = tableService.create(request);

        // when
        final List<OrderTableResponse> responses = tableService.list();

        // then
        final OrderTable orderTable = orderTableRepository.findById(orderTableId).get();
        assertSoftly(softly -> {
            softly.assertThat(responses).isNotEmpty();
            softly.assertThat(orderTable.id()).isEqualTo(orderTableId);
            softly.assertThat(orderTable.numberOfGuests()).isEqualTo(numberOfGuests);
            softly.assertThat(orderTable.isEmpty()).isEqualTo(isEmpty);
        });
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블인지 여부를 수정할 시, ")
    class ChangeEmptyOrderTable {
        @Test
        @DisplayName("정상적으로 수정한다")
        void changeEmpty() {
            // given
            final OrderTableCreateRequest request = new OrderTableCreateRequest(1, true);
            final Long orderTableId = tableService.create(request);
            final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

            // when, then
            assertThatNoException()
                    .isThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest));
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 시 예외 발생")
        void notExistOrderTableException() {
            // given
            final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(-1L, orderTableChangeEmptyRequest));
        }

        @Test
        @DisplayName("테이블 그룹 아이디가 null이 아닐 시 예외 발생")
        void alreadyExistTableGroupException() {
            // given
            final OrderTableCreateRequest requestA = new OrderTableCreateRequest(1, true);
            final Long orderTableIdA = tableService.create(requestA);
            final OrderTableCreateRequest requestB = new OrderTableCreateRequest(2, true);
            final Long orderTableIdB = tableService.create(requestB);
            final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

            final TableGroupCreateRequest tableGroupCreateRequest =
                    new TableGroupCreateRequest(List.of(orderTableIdA, orderTableIdB));
            tableGroupService.create(tableGroupCreateRequest);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(orderTableIdA, orderTableChangeEmptyRequest));
        }

        @Test
        @DisplayName("주문 테이블의 상태가 요리 또는 식사중일 시 예외 발생")
        void orderStatusNotCompletionException() {
            // given
            final OrderTableCreateRequest request = new OrderTableCreateRequest(1, false);
            final Long orderTableId = tableService.create(request);
            final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(true);

            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 2);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTableId,
                    List.of(orderLineItemCreateRequest)
            );

            orderService.create(orderCreateRequest);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeEmpty(orderTableId, orderTableChangeEmptyRequest));
        }
    }

    @Nested
    @DisplayName("주문 테이블의 손님 수를 수정할 때, ")
    class UpdateGuestCount {
        @Test
        @DisplayName("정상적으로 수정할 수 있다")
        void updateGuestCount() {
            // given
            final int numberOfGuests = 2;
            final OrderTableCreateRequest request = new OrderTableCreateRequest(1, false);
            final Long orderTableId = tableService.create(request);
            final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest =
                    new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);

            // when
            tableService.changeNumberOfGuests(orderTableId, orderTableChangeNumberOfGuestsRequest);

            // then
            final OrderTable saved = orderTableRepository.findById(orderTableId).get();
            assertThat(saved.numberOfGuests()).isEqualTo(numberOfGuests);
        }

        @Test
        @DisplayName("주문 테이블의 손님 수가 0 미만일 시 예외 발생")
        void guestCountLessThanZeroException() {
            // given
            final int numberOfGuests = -1;
            final OrderTableCreateRequest request = new OrderTableCreateRequest(2, false);
            final Long orderTableId = tableService.create(request);
            final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest =
                    new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeNumberOfGuests(
                            orderTableId,
                            orderTableChangeNumberOfGuestsRequest
                    ));
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 시 예외 발생")
        void orderTableNotExistException() {
            // given
            final int numberOfGuests = 2;
            final OrderTableCreateRequest request = new OrderTableCreateRequest(1, false);
            final Long orderTableId = tableService.create(request);
            final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest =
                    new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeNumberOfGuests(
                            orderTableId + 1,
                            orderTableChangeNumberOfGuestsRequest)
                    );
        }

        @Test
        @DisplayName("주문 테이블이 비어있을 시 예외 발생")
        void orderTableEmptyException() {
            // given
            final int numberOfGuests = 2;
            final OrderTableCreateRequest request = new OrderTableCreateRequest(1, true);
            final Long orderTableId = tableService.create(request);
            final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest =
                    new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId,
                            orderTableChangeNumberOfGuestsRequest));
        }
    }
}
