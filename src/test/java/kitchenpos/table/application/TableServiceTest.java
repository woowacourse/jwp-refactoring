package kitchenpos.table.application;

import static kitchenpos.order.fixture.OrderFixture.createOrder;
import static kitchenpos.table.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.table.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.ui.dto.request.TableCreateRequest;
import kitchenpos.table.ui.dto.response.TableCreateResponse;
import kitchenpos.table.ui.dto.response.TableFindAllResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create_success() {
        // given
        TableCreateRequest request = new TableCreateRequest(4, true);

        // when
        TableCreateResponse response = tableService.create(request);

        // then
        OrderTable dbOrderTable = orderTableRepository.findById(response.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbOrderTable.getId()).isEqualTo(response.getId());
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list_success() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(4, true));

        // when
        List<TableFindAllResponse> responses = tableService.list();

        // then
        List<Long> tableIds = responses.stream()
                .map(TableFindAllResponse::getId)
                .collect(Collectors.toList());
        assertThat(tableIds).contains(savedOrderTable.getId());
    }

    @DisplayName("테이블 상태를 변경한다.")
    @Test
    void changeEmpty_success() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(4, false));

        // when
        tableService.changeEmpty(savedOrderTable.getId(), new OrderTableChangeEmptyRequest(true));

        // then
        OrderTable changedTable = orderTableRepository.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 상태를 변경할 때 수정하려는 주문테이블이 단체 지정되어있으면 예외를 반환한다.")
    @Test
    void changeEmpty_false_if_already_tableGroup() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(createOrderTable(4, true));
        OrderTable orderTable2 = orderTableRepository.save(createOrderTable(4, true));
        TableGroup tableGroup = tableGroupRepository.save(
                createTableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)));
        orderTable1.group(tableGroup.getId());
        orderTableRepository.save(orderTable1);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), new OrderTableChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태를 변경할 때 수정하려는 주문테이블의 상태가 COOKING 또는 MEAL이라면 예외를 반환한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void changeEmpty_false_if_orderTableStatus_is_COOKING_or_MEAL(String status) {
        // given
        OrderTable orderTable = orderTableRepository.save(createOrderTable(4, true));
        orderRepository.save(createOrder(orderTable.getId(), OrderStatus.valueOf(status), LocalDateTime.now()));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableChangeEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문한 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(4, false));

        // when
        int numberOfGuests = 3;
        tableService.changeNumberOfGuests(savedOrderTable.getId(),
                new OrderTableChangeNumberOfGuestsRequest(numberOfGuests));

        // then
        OrderTable changedTable = orderTableRepository.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("테이블의 방문한 손님수를 변경할 때 테이블이 비어있으면 예외를 발생한다.")
    @Test
    void changeNumberOfGuests_fail_if_orderTable_is_empty() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(4, true));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(),
                new OrderTableChangeNumberOfGuestsRequest(3)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
