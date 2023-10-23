package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.TableGroup;
import kitchenpos.dto.ordertable.OrderTableChangeGuestRequest;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.support.DataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private DataCleaner dataCleaner;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableService tableService;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create_orderTable() {
        // given
        final OrderTableRequest request = new OrderTableRequest(5);

        // when
        final OrderTableResponse result = tableService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            softly.assertThat(result.getTableGroupId()).isNull();
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuest());
            softly.assertThat(result.isEmpty()).isFalse();
        });
    }

    @DisplayName("전체 주문 테이블을 조회한다.")
    @Test
    void find_all_orderTable() {
        // given
        orderTableRepository.save(new OrderTable(4));
        orderTableRepository.save(new OrderTable(5));

        // when
        final List<OrderTableResponse> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 테이블의 상태를 변경할 수 있다.")
    @Test
    void change_orderTable_empty() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(4));
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 2));
        final Order order = new Order(savedOrderTable.getId());
        order.changeStatus(OrderStatus.COMPLETION.name());
        order.addOrderLineItems(orderLineItems.get(0));
        orderRepository.save(order);

        // when
        final OrderTableResponse result = tableService.changeEmpty(savedOrderTable.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(savedOrderTable.getId());
            softly.assertThat(result.getTableGroupId()).isNull();
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
            softly.assertThat(result.isEmpty()).isTrue();
        });
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_not_exist_orderTable() {
        // given
        final Long wrongOrderTableId = 0L;

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(wrongOrderTableId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 테이블 그룹이 존재하면 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_not_null_group() {
        // given
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.forSave());
        final OrderTable alreadyContainedOrderTable = orderTableRepository.save(new OrderTable(null, tableGroup, 5, false));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(alreadyContainedOrderTable.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 COOKING이거나 MEAL인 경우 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_invalid_orderStatus() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(4));
        orderRepository.save(new Order(savedOrderTable.getId()));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change_number_of_guests() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(4));
        final OrderTableChangeGuestRequest request = new OrderTableChangeGuestRequest(3);

        // when
        final OrderTableResponse result = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(savedOrderTable.getId());
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        });
    }

    @DisplayName("변경할 주문 테이블의 손님 수가 음수이면 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_with_number_of_guests_is_negative() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(4));
        final OrderTableChangeGuestRequest wrongRequest = new OrderTableChangeGuestRequest(-1);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), wrongRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않는 경우 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_with_not_found_orderTable() {
        // given
        final Long wrongOrderTableId = 0L;
        final OrderTableChangeGuestRequest request = new OrderTableChangeGuestRequest(-1);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(wrongOrderTableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 손님 수를 변경할 수 없다.")
    @Test
    void change_number_of_guests_fail_with_empty_orderTable() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, null, 4, true));
        final OrderTableChangeGuestRequest request = new OrderTableChangeGuestRequest(5);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
