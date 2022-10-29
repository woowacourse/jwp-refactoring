package kitchenpos.application;

import static kitchenpos.DomainFixture.getMenuGroup;
import static kitchenpos.DtoFixture.getEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getNotEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getOrderCreateRequest;
import static kitchenpos.DtoFixture.getTableChangeEmptyRequest;
import static kitchenpos.DtoFixture.getTableChangeNumberOfGuestsRequest;
import static kitchenpos.DtoFixture.getTableCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.order.OrderStatusChangeRequest;
import kitchenpos.ui.request.table.TableChangeEmptyRequest;
import kitchenpos.ui.request.table.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.request.table.TableCreateRequest;
import kitchenpos.ui.request.tablegroup.TableGroupCreatRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        final TableCreateRequest request = getEmptyTableCreateRequest();

        final OrderTable savedTable = 테이블_등록(request);

        assertAll(
                () -> assertThat(savedTable.getId()).isNotNull(),
                () -> assertThat(savedTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        테이블_등록(getEmptyTableCreateRequest());

        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(1);
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        final OrderTable savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final Menu menu = 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));
        final Order savedOrder = 주문_등록(getOrderCreateRequest(savedTable.getId(), menu.getId()));
        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(
                OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest);

        final TableChangeEmptyRequest request = getTableChangeEmptyRequest(true);

        final OrderTable changedTable = tableService.changeEmpty(savedTable.getId(), request);

        assertAll(
                () -> assertThat(changedTable.getId()).isEqualTo(savedTable.getId()),
                () -> assertThat(changedTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("빈 테이블로 변경한다. - 존재하지 않는 테이블이면 예외를 반환한다.")
    @Test
    void changeEmpty_exception_noSuchTable() {
        final TableChangeEmptyRequest request = getTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(null, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경한다. - 단체 지정에 포함된 테이블이면 예외를 반환한다.")
    @Test
    void changeEmpty_exception_alreadyInTableGroup() {
        final OrderTable orderTable = 테이블_등록(getEmptyTableCreateRequest());
        final OrderTable anotherTable = 테이블_등록(getEmptyTableCreateRequest());

        final TableGroupCreatRequest groupCreatRequest = getTableCreateRequest(
                List.of(orderTable.getId(), anotherTable.getId()));
        tableGroupService.create(groupCreatRequest);

        final TableChangeEmptyRequest request = getTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경한다. - 주문 상태가 COOKING이거나 MEAL이면 예외를 반환한다.")
    @Test
    void changeEmpty_exception_orderStatusIsCookingOrMeal() {
        final OrderTable savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        final Menu menu = 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));
        주문_등록(getOrderCreateRequest(savedTable.getId(), menu.getId()));

        final TableChangeEmptyRequest request = getTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final TableChangeNumberOfGuestsRequest request = getTableChangeNumberOfGuestsRequest(5);

        final OrderTable changedTable = tableService.changeNumberOfGuests(savedTable.getId(), request);

        assertAll(
                () -> assertThat(changedTable.getId()).isEqualTo(savedTable.getId()),
                () -> assertThat(changedTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(changedTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("방문한 손님 수를 변경한다. - 손님의 수가 0보다 작으면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_exception_numberOfGuestsIsLessThanZero() {
        final OrderTable savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final TableChangeNumberOfGuestsRequest request = getTableChangeNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다. - 존재하지 않는 주문 테이블이면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_exception_noSuchTable() {
        final TableChangeNumberOfGuestsRequest request = getTableChangeNumberOfGuestsRequest(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(null, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다. - 빈 테이블이면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_exception_tableIsEmpty() {
        final OrderTable savedTable = 테이블_등록(getEmptyTableCreateRequest());
        final TableChangeNumberOfGuestsRequest request = getTableChangeNumberOfGuestsRequest(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
