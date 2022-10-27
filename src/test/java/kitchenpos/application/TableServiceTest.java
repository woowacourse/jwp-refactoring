package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.MenuRepository;
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
    private OrderTableDao tableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create_success() {
        // given
        OrderTable orderTable = createOrderTable(4, true);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        OrderTable dbOrderTable = tableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbOrderTable.getId()).isEqualTo(savedOrderTable.getId());
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list_success() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, true));

        // when
        List<OrderTable> tables = tableService.list();

        // then
        List<Long> tableIds = tables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        assertThat(tableIds).contains(savedOrderTable.getId());
    }

    @DisplayName("테이블 상태를 변경한다.")
    @Test
    void changeEmpty_success() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, false));

        // when
        tableService.changeEmpty(savedOrderTable.getId(), createOrderTable(true));

        // then
        OrderTable changedTable = tableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 상태를 변경할 때 수정하려는 주문테이블이 단체 지정되어있으면 예외를 반환한다.")
    @Test
    void changeEmpty_false_if_already_tableGroup() {
        // given
        OrderTable orderTable1 = tableDao.save(createOrderTable(4, true));
        OrderTable orderTable2 = tableDao.save(createOrderTable(4, true));
        TableGroup tableGroup = tableGroupDao.save(createTableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)));
        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable1.setEmpty(false);
        tableDao.save(orderTable1);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), createOrderTable(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태를 변경할 때 수정하려는 주문테이블의 상태가 COOKING 또는 MEAL이라면 예외를 반환한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void changeEmpty_false_if_orderTableStatus_is_COOKING_or_MEAL(String status) {
        // given
        Menu menu = menuRepository.save(createMenu("후라이드+후라이드", 19_000L, 1L,
                Collections.singletonList(createMenuProduct(1L, 2))));
        OrderTable orderTable = tableDao.save(createOrderTable(4, true));
        orderDao.save(createOrder(orderTable.getId(), status, LocalDateTime.now(),
                Collections.singletonList(createOrderLineItem(menu.getId(), 1))));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTable(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문한 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, false));

        // when
        int numberOfGuests = 3;
        tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(numberOfGuests));

        // then
        OrderTable changedTable = tableDao.findById(savedOrderTable.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("테이블의 방문한 손님수를 변경할 때 손님수가 음수이면 에러를 반환한다.")
    @Test
    void changeNumberOfGuests_fail_if_numberOfGuests_is_negative() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, false));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문한 손님수를 변경할 때 테이블이 비어있으면 예외를 발생한다.")
    @Test
    void changeNumberOfGuests_fail_if_orderTable_is_empty() {
        // given
        OrderTable savedOrderTable = tableDao.save(createOrderTable(4, true));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), createOrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
