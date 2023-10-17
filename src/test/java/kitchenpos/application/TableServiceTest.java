package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.IllegalOrderStatusException;
import kitchenpos.exception.IllegalOrderTableGuestNumberException;
import kitchenpos.exception.OrderTableNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableServiceTest extends ServiceBaseTest {

    @Autowired
    protected TableService tableService;

    @Autowired
    protected OrderService orderService;

    @BeforeEach
    void setUp() {
//        orderTable = Fixture.orderTable(null, 3, false);
    }

    @Test
    @DisplayName("비어있는 주문 테이블을 생성할 수 있다.")
    void create() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);

        //when
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        //then
        assertAll(
                () -> assertThat(orderTableResponse).isNotNull(),
                () -> assertThat(orderTableResponse.isEmpty()).isTrue(),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuest())
        );
    }

    @Test
    @DisplayName("주문 테이블을 조회할 수 있다.")
    void list() {
        //given
        tableService.create(new OrderTableRequest(3, false));
        tableService.create(new OrderTableRequest(3, false));

        //when
        final List<OrderTableResponse> orderTableResponses = tableService.list();

        //then
        assertThat(orderTableResponses).hasSize(2);
    }

    @Test
    @DisplayName("주문 테이블을 빈 상태로 변경할 수 있다.")
    void changeEmpty() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        final OrderTableRequest orderTableRequest2 = new OrderTableRequest(3, true);

        //when
        final OrderTableResponse response = tableService.changeEmpty(orderTableResponse.getId(), orderTableRequest2);

        //then
        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블은 존재해야한다.")
    void changeEmptyValidOrderTable() {
        //when&then
        assertThatThrownBy(() -> tableService.changeEmpty(999L, null))
                .isInstanceOf(OrderTableNotFoundException.class)
                .hasMessage("OrderTable을 찾을 수 없습니다.");
    }

//    @Test
//    @DisplayName("빈상태로 변경시 orderTable의 테이블 그룹 아이디가 null이 아니면 안된다.")
//    void changeEmptyValidTableGroupId() {
//        //given
//        final OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
//        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
//        final OrderTableRequest orderTableRequest2 = new OrderTableRequest(3, true);
//
//
//
//        final OrderTable savedOrderTable1 = orderTableDao.save(Fixture.orderTable(null, 10, true));
//        final OrderTable savedOrderTable2 = orderTableDao.save(Fixture.orderTable(null, 5, true));
//        final TableGroup savedTableGroup = tableGroupDao.save(Fixture.orderTableGroup(LocalDateTime.now(), List.of(savedOrderTable1, savedOrderTable2)));
//        final OrderTable savedOrderTable = orderTableDao.save(Fixture.orderTable(savedTableGroup.getId(), 10, true));
//
//        //when&then
//        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
//                .isInstanceOf(IllegalArgumentException.class);
//    }

    @ParameterizedTest(name = "주문 테이블이 요리, 식사 상태이면 안된다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmptyValidStatus(final OrderStatus orderStatus) {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        final Menu menu = menuRepository.save(new Menu("메뉴1", new BigDecimal(1000), menuGroup));
        final OrderRequest orderRequest = new OrderRequest(orderTableResponse.getId(), List.of(new OrderLineItemRequest(menu.getId(), 3L)));
        final OrderResponse orderResponse = orderService.create(orderRequest);
        final OrderResponse changeOrderResponse = orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(orderStatus));

        //when&then
        assertThatThrownBy(() -> tableService.changeEmpty(changeOrderResponse.getId(), orderTableRequest))
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessage("잘못된 주문 상태입니다.");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        final OrderTableRequest changedOrderTableRequest = new OrderTableRequest(1, true);

        //when
        final OrderTableResponse changedOrderTableResponse = tableService.changeNumberOfGuests(orderTableResponse.getId(), changedOrderTableRequest);

        //then
        assertThat(changedOrderTableResponse.getNumberOfGuests()).isEqualTo(changedOrderTableRequest.getNumberOfGuest());
    }

    @Test
    @DisplayName("주문 테이블의 손님 수는 0 이상이어야 한다.")
    void changeNumberOfGuestsOverZero() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        final OrderTableRequest changedOrderTableRequest = new OrderTableRequest(-1, true);

        //when&then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), changedOrderTableRequest))
                .isInstanceOf(IllegalOrderTableGuestNumberException.class)
                .hasMessage("잘못된 주문 테이블 게스트 숫자 설정입니다.");
    }

    @Test
    @DisplayName("주문 테이블은 존재해야 한다.")
    void changeNumberOfGuestValidOrderTable() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);

        //when&then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(999L, orderTableRequest))
                .isInstanceOf(OrderTableNotFoundException.class)
                .hasMessage("OrderTable을 찾을 수 없습니다.");
    }
}
