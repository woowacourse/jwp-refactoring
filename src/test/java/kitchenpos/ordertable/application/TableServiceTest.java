package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.dto.OrderTableChangeGuestRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeStatusRequest;
import kitchenpos.ordertable.application.dto.OrderTableRequest;
import kitchenpos.ordertable.application.dto.OrderTableResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.support.DataCleaner;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
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
        final OrderTableRequest request = new OrderTableRequest(5, false);

        // when
        final OrderTableResponse result = tableService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            softly.assertThat(result.getTableGroupId()).isNull();
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
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
        final Order order = new Order(savedOrderTable.getId());
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(order, 1L, 2));
        order.changeStatus(OrderStatus.COMPLETION.name());
        orderRepository.save(order);
        final OrderTableChangeStatusRequest request = new OrderTableChangeStatusRequest(true);

        // when
        final OrderTableResponse result = tableService.changeEmpty(savedOrderTable.getId(), request);

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
        final OrderTableChangeStatusRequest request = new OrderTableChangeStatusRequest(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(wrongOrderTableId, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("주문 테이블에 테이블 그룹이 존재하면 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_not_null_group() {
        // given
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.forSave());
        final OrderTable alreadyContainedOrderTable = orderTableRepository.save(new OrderTable(null, tableGroup, 5, false));
        final OrderTableChangeStatusRequest request = new OrderTableChangeStatusRequest(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(alreadyContainedOrderTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 테이블 그룹에 속한 주문 테이블입니다.");
    }

    @DisplayName("주문 테이블의 상태가 COOKING이거나 MEAL인 경우 주문 테이블의 상태를 변경할 수 없다.")
    @Test
    void change_orderTable_empty_fail_with_invalid_orderStatus() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(4));
        orderRepository.save(new Order(savedOrderTable.getId()));
        final OrderTableChangeStatusRequest request = new OrderTableChangeStatusRequest(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("요리중이거나 식사중인 주문 테이블은 상태를 변경할 수 없습니다.");
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
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("변경할 손님 수는 음수이면 안됩니다.");
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
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 주문 테이블입니다.");
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
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문테이블이 비어있는 상태이면 손님 수를 변경할 수 없습니다.");
    }
}
