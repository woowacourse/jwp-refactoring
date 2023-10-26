package kitchenpos.ordertable.application;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_CHANGE_EMPTY_REQUEST;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_CHANGE_GUEST_REQUEST;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_CREATE_REQUEST;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import kitchenpos.common.ServiceTest;
import kitchenpos.common.fixtures.OrderTableFixtures;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeGuestRequest;
import kitchenpos.ordertable.application.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.application.dto.OrderTableResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderTableServiceTest extends ServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 테이블 생성 시 테이블 그룹 ID를 null로 설정하여 저장한다.")
    void create() {
        // given
        final OrderTableCreateRequest request = ORDER_TABLE1_CREATE_REQUEST();
        int numberOfGuests = request.getNumberOfGuests();
        boolean empty = request.isEmpty();

        // when
        final OrderTableResponse response = orderTableService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
            softly.assertThat(response.isEmpty()).isEqualTo(empty);
            softly.assertThat(response.getTableGroup().getId()).isNull();
            softly.assertThat(response.getTableGroup().getCreatedDate()).isNull();
        });
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블 여부 변경 시")
    class ChangeEmpty {

        @Test
        @DisplayName("빈 테이블 여부 변경에 성공한다.")
        void success() {
            // given
            final OrderTableChangeEmptyRequest request = ORDER_TABLE1_CHANGE_EMPTY_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            boolean beforeEmpty = savedOrderTable.isEmpty();

            // when
            final OrderTableResponse response = orderTableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertThat(response.isEmpty()).isNotEqualTo(beforeEmpty);
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_notFoundOrderTable() {
            // given
            final Long notExistOrderTableId = -1L;
            final OrderTableChangeEmptyRequest request = ORDER_TABLE1_CHANGE_EMPTY_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> orderTableService.changeEmpty(notExistOrderTableId, request))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("주문 테이블의 손님 수 변경 시")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("변경에 성공한다.")
        void success() {
            // given
            final OrderTableChangeGuestRequest request = ORDER_TABLE1_CHANGE_GUEST_REQUEST();
            final OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            int beforeNumberOfGuests = savedOrderTable.getNumberOfGuests();

            // when
            OrderTableResponse response = orderTableService.changeNumberOfGuests(orderTable.getId(), request);
            // then
            assertSoftly(softly -> {
                softly.assertThat(response.getNumberOfGuests()).isNotEqualTo(beforeNumberOfGuests);
                softly.assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            });
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
        void throws_orderTableNotExist() {
            // given
            final Long notExistOrderTableId = -1L;
            final OrderTableChangeGuestRequest request = ORDER_TABLE1_CHANGE_GUEST_REQUEST();
            final OrderTable orderTable = OrderTableFixtures.ORDER_TABLE1();
            orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(notExistOrderTableId, request))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }
    }
}
