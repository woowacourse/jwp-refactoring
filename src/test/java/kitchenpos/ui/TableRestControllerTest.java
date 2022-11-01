package kitchenpos.ui;

import static fixture.MenuFixtures.후라이드치킨_메뉴;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import common.IntegrationTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.request.TableGroupRequest;
import kitchenpos.ui.request.TableIdRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class TableRestControllerTest {

    @Autowired
    private TableRestController sut;

    @Autowired
    private OrderRestController orderRestController;

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @DisplayName("테이블의 상태 변경 시 테이블은 존재해야 한다.")
    @Test
    void tableMustExistWhenChangeEmptyStatus() {
        // arrange
        long notFoundTableId = -1L;

        // act & assert
        assertThatThrownBy(() -> changeOrderTableStatus(notFoundTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 상태 변경 시 테이블 그룹에 묶여 있으면 안된다.")
    @Test
    void tableMustNotIncludeToTableGroup() {
        // arrange
        groupTables(1L, 2L);

        // act & assert
        assertThatThrownBy(() -> changeOrderTableStatus(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 상태 변경 시 계산 완료여야 한다.")
    @Test
    void tableMustCompletion() {
        // arrange
        changeOrderTableStatus(1L, false);

        createOrder(1L, new OrderLineItemRequest(후라이드치킨_메뉴.id(), 1L));

        // act & assert
        assertThatThrownBy(() -> changeOrderTableStatus(1L, true))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("방문 손님 수는 0명 이상이어야 한다.")
    @Test
    void numberOfGuestMustOverZero() {
        assertThatThrownBy(() -> changeNumberOfGuest(1L, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 방문 손님 수는 변경할 수 없다.")
    @Test
    void changeNumberOfGuestToEmptyTable() {
        assertThatThrownBy(() -> changeNumberOfGuest(1L, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블의 방문 손님 수는 변경할 수 없다.")
    @Test
    void changeNumberOfGuestToNotFoundTable() {
        long notFoundTableId = -1L;

        assertThatThrownBy(() -> changeNumberOfGuest(notFoundTableId, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void groupTables(long... tableIds) {
        List<TableIdRequest> orderTables = Arrays.stream(tableIds)
                .mapToObj(TableIdRequest::new)
                .collect(Collectors.toList());
        TableGroupRequest request = new TableGroupRequest(orderTables);
        tableGroupRestController.create(request);
    }

    private void changeOrderTableStatus(long orderTableId, boolean isEmpty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(isEmpty);
        sut.changeEmpty(orderTableId, orderTableRequest);
    }

    private void createOrder(long orderTableId, OrderLineItemRequest... itemRequests) {
        final OrderRequest request = new OrderRequest(orderTableId, List.of(itemRequests));
        orderRestController.create(request);
    }

    private void changeNumberOfGuest(long orderTableId, int numberOfGuest) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuest);
        sut.changeNumberOfGuests(orderTableId, request);
    }
}
