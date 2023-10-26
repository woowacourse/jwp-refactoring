package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.table.TableEmptyChangeRequest;
import kitchenpos.application.dto.table.TableGuestChangeRequest;
import kitchenpos.application.dto.table.TableRequest;
import kitchenpos.application.dto.table.TableResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.support.DataDependentIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends DataDependentIntegrationTest {

    private static final long NOT_EXIST_ID = Long.MAX_VALUE;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private TableService tableService;

    @DisplayName("새 주문 테이블을 저장한다.")
    @Test
    void create() {
        // given
        final TableRequest tableRequest = new TableRequest(3, true);

        // when
        final TableResponse savedOrderTable = tableService.create(tableRequest);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(TableResponse.from(new OrderTable(3, true)));
    }

    @DisplayName("모든 주문 테이블을 조회한다.")
    @Test
    void list() {
        // given
        final TableRequest tableRequest = new TableRequest(2, true);
        final TableResponse savedOrderTable1 = tableService.create(tableRequest);
        final TableRequest tableRequest2 = new TableRequest(3, false);
        final TableResponse savedOrderTable2 = tableService.create(tableRequest2);

        // when
        final List<TableResponse> list = tableService.list();

        // then
        assertThat(list).usingRecursiveComparison()
            .isEqualTo(List.of(savedOrderTable1, savedOrderTable2));
    }

    @DisplayName("주문 테이블을 비어있도록 변경한다.")
    @Test
    void changeEmpty_success() {
        // given
        final TableRequest tableRequest = new TableRequest(3, false);
        final TableResponse savedOrderTable = tableService.create(tableRequest);
        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        // when
        final TableResponse result = tableService.changeEmpty(savedOrderTable.getId(), request);

        // then
        assertThat(result.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 주문 테이블을 변경 요청하면 예외가 발생한다.")
    @Test
    void changeEmpty_notExistTableId_fail() {
        // given
        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(NOT_EXIST_ID, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 그룹에 속해있다면 변경 요청 시 예외가 발생한다.")
    @Test
    void changeEmpty_tableInGroup_fail() {
        // given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = new OrderTable(3, true);
        orderTable.groupBy(tableGroup);
        orderTableRepository.save(orderTable);
        final Long groupedOrderTableId = orderTable.getId();

        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(groupedOrderTableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 COMPLETION 이 아니라면 변경 시 예외가 발생한다.")
    @Test
    void changeEmpty_tableNotComplete_fail() {
        // given
        final TableRequest tableRequest = new TableRequest(3, false);
        final TableResponse savedOrderTable = tableService.create(tableRequest);
        final OrderTable orderTable = orderTableRepository.findById(savedOrderTable.getId()).get();
        orderRepository.save(new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(new OrderLineItem(createMenuAndGetId(), 1))));
        final Long orderTableId = orderTable.getId();

        final TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private long createMenuAndGetId() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Menu menu = menuRepository.save(new Menu("menu", Price.from(BigDecimal.valueOf(1000L)), menuGroup));

        return menu.getId();
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        final TableRequest tableRequest = new TableRequest(2, false);
        final TableResponse savedOrderTable = tableService.create(tableRequest);

        final TableGuestChangeRequest request = new TableGuestChangeRequest(3);

        // when
        final TableResponse result = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

        // then
        assertThat(result.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("주문 테이블의 변경하려는 손님 수가 0보다 작으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_wrongGuest_fail() {
        // given
        final TableRequest tableRequest = new TableRequest(2, false);
        final TableResponse savedOrderTable = tableService.create(tableRequest);
        final Long orderTableId = savedOrderTable.getId();

        final TableGuestChangeRequest request = new TableGuestChangeRequest(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경하려는 주문 테이블이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_notExistTable_fail() {
        // given
        final TableGuestChangeRequest request = new TableGuestChangeRequest(3);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(NOT_EXIST_ID, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경하려는 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_empty_fail() {
        // given
        final TableRequest tableRequest = new TableRequest(2, true);
        final TableResponse savedOrderTable = tableService.create(tableRequest);
        final Long orderTableId = savedOrderTable.getId();

        final TableGuestChangeRequest request = new TableGuestChangeRequest(3);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
