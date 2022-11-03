package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.exception.AlreadyGroupedException;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.NumberOfGuestsSizeException;
import kitchenpos.exception.OrderNotCompletionException;
import kitchenpos.exception.TableEmptyException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.support.ServiceTest;
import kitchenpos.support.fixtures.MenuFixtures;
import kitchenpos.support.fixtures.MenuGroupFixtures;
import kitchenpos.support.fixtures.OrderFixtures;
import kitchenpos.support.fixtures.OrderLineItemFixtures;
import kitchenpos.support.fixtures.OrderTableFixtures;
import kitchenpos.support.fixtures.TableGroupFixtures;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:truncate.sql")
@SpringBootTest
class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문 테이블을 생성한다")
    void create() {
        // given
        final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(0, true);

        // when
        final OrderTableResponse saved = tableService.create(orderTableCreateRequest);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("모든 주문 테이블을 조회한다")
    void list() {
        // given
        final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, false);
        final OrderTableResponse saved = tableService.create(orderTableCreateRequest);

        // when
        final List<OrderTableResponse> orderTables = tableService.list();
        // then
        assertAll(
                () -> assertThat(orderTables).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orderTables).extracting("id")
                        .contains(saved.getId())
        );
    }

    @Test
    @DisplayName("주문 테이블의 주문 가능 여부를 변경한다")
    void changeEmpty() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu.getId(), 2);
        final Order order = OrderFixtures.COMPLETION_ORDER.createWithOrderTableIdAndOrderLineItems(
                savedOrderTable.getId(), orderLineItem);
        orderRepository.save(order);

        // when
        final OrderTableResponse changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), false);

        // then
        assertAll(
                () -> assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests()),
                () -> assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 주문 테이블의 주문 가능 여부를 변경할 때 예외가 발생한다")
    void changeEmptyExceptionNotExistOrderTable() {
        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, true))
                .isExactlyInstanceOf(NotFoundOrderException.class);
    }

    @Test
    @DisplayName("주문 테이블이 단체로 지정되어 있디면 주문 테이블의 주문 가능 여부를 변경할 때 예외가 발생한다")
    void changeEmptyExceptionGroupedOrderTable() {
        // given
        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(savedTableGroup);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), true))
                .isExactlyInstanceOf(AlreadyGroupedException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING_ORDER", "MEAL_ORDER"})
    @DisplayName("주문이 상태가 COOKING, MEAL인 경우 주문 테이블의 주문 가능 여부를 변경할 때 예외가 발생한다")
    void changeEmptyExceptionNotCompletionOrder(final OrderFixtures orderFixtures) {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu.getId(), 2);
        final Order order = orderFixtures.createWithOrderTableIdAndOrderLineItems(
                savedOrderTable.getId(), orderLineItem);
        orderRepository.save(order);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), true))
                .isExactlyInstanceOf(OrderNotCompletionException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 숫자를 변경한다")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // when
        final OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), 3);

        // then
        assertAll(
                () -> assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(changedOrderTable.isEmpty()).isFalse(),
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("변경하려는 손님 숫자가 음수이면 주문 테이블의 손님 숫자를 변경할 때 예외가 발생한다")
    void changeNumberOfGuestsExceptionNegativeNumberOfGuests() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), -1))
                .isExactlyInstanceOf(NumberOfGuestsSizeException.class);
    }

    @Test
    @DisplayName("변경하려는 주문 테이블이 존재하지 않으면 주문 테이블의 손님 숫자를 변경할 때 예외가 발생한다")
    void changeNumberOfGuestsExceptionNotExistOrderTable() {
        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, 3))
                .isExactlyInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("변경하려는 주문 테이블이 주문을 동록할 수 없다면 주문 테이블의 손님 숫자를 변경할 때 예외가 발생한다")
    void changeNumberOfGuestsExceptionNotEmptyOrderTable() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createEmptyTable(null);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), 1))
                .isExactlyInstanceOf(TableEmptyException.class);
    }
}
