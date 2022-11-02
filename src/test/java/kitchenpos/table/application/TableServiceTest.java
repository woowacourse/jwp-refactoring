package kitchenpos.table.application;

import static kitchenpos.DtoFixture.getEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getMenuGroupCreateRequest;
import static kitchenpos.DtoFixture.getNotEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getOrderCreateRequest;
import static kitchenpos.DtoFixture.getTableChangeEmptyRequest;
import static kitchenpos.DtoFixture.getTableChangeNumberOfGuestsRequest;
import static kitchenpos.DtoFixture.getTableCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.product.domain.OrderStatus;
import kitchenpos.table.dto.request.TableChangeEmptyRequest;
import kitchenpos.table.dto.request.TableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.request.TableCreateRequest;
import kitchenpos.table.dto.request.TableGroupCreatRequest;
import kitchenpos.table.dto.response.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        final TableCreateRequest request = getEmptyTableCreateRequest();

        final TableResponse savedTable = 테이블_등록(request);

        assertAll(
                () -> assertThat(savedTable.getId()).isNotNull(),
                () -> assertThat(savedTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        테이블_등록(getEmptyTableCreateRequest());

        final List<TableResponse> orderTables = tableService.list();

        assertThat(orderTables).hasSize(1);
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        final TableResponse savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final MenuGroupResponse menuGroup = 메뉴_그룹_등록(getMenuGroupCreateRequest());
        final MenuResponse menu = 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));
        final OrderResponse savedOrder = 주문_등록(getOrderCreateRequest(savedTable.getId(), menu.getId()));
        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(
                OrderStatus.COMPLETION);
        orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest);

        final TableChangeEmptyRequest request = getTableChangeEmptyRequest(true);

        final TableResponse changedTable = tableService.changeEmpty(savedTable.getId(), request);

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
        final TableResponse orderTable = 테이블_등록(getEmptyTableCreateRequest());
        final TableResponse anotherTable = 테이블_등록(getEmptyTableCreateRequest());

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
        final TableResponse savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final MenuGroupResponse menuGroup = 메뉴_그룹_등록(getMenuGroupCreateRequest());
        final MenuResponse menu = 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));
        주문_등록(getOrderCreateRequest(savedTable.getId(), menu.getId()));

        final TableChangeEmptyRequest request = getTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final TableResponse savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final TableChangeNumberOfGuestsRequest request = getTableChangeNumberOfGuestsRequest(5);

        final TableResponse changedTable = tableService.changeNumberOfGuests(savedTable.getId(), request);

        assertAll(
                () -> assertThat(changedTable.getId()).isEqualTo(savedTable.getId()),
                () -> assertThat(changedTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(changedTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("방문한 손님 수를 변경한다. - 손님의 수가 0보다 작으면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_exception_numberOfGuestsIsLessThanZero() {
        final TableResponse savedTable = 테이블_등록(getNotEmptyTableCreateRequest(0));
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
        final TableResponse savedTable = 테이블_등록(getEmptyTableCreateRequest());
        final TableChangeNumberOfGuestsRequest request = getTableChangeNumberOfGuestsRequest(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
