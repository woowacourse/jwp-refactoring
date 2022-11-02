package kitchenpos.application;

import static kitchenpos.fixture.domain.MenuFixture.createMenu;
import static kitchenpos.fixture.domain.OrderFixture.createOrder;
import static kitchenpos.fixture.domain.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.domain.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.TableCreateRequest;
import kitchenpos.ui.dto.response.TableCreateResponse;
import kitchenpos.ui.dto.response.TableFindAllResponse;
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

    @Autowired
    private MenuRepository menuRepository;

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
        Menu menu = menuRepository.save(createMenu());
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
        tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(numberOfGuests));

        // then
        OrderTable changedTable = orderTableRepository.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("테이블의 방문한 손님수를 변경할 때 손님수가 음수이면 에러를 반환한다.")
    @Test
    void changeNumberOfGuests_fail_if_numberOfGuests_is_negative() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(4, false));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문한 손님수를 변경할 때 테이블이 비어있으면 예외를 발생한다.")
    @Test
    void changeNumberOfGuests_fail_if_orderTable_is_empty() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(4, true));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
