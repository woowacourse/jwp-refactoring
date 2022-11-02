package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.support.application.ServiceTestEnvironment;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableGuestNumberRequest;
import kitchenpos.dto.request.OrderTableSetEmptyRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.exception.EmptyOrderTableException;
import kitchenpos.exception.InvalidNumberOfGuestsException;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupNullException;
import kitchenpos.support.fixture.MenuFixture;
import kitchenpos.support.fixture.MenuGroupFixture;
import kitchenpos.support.fixture.OrderFixture;
import kitchenpos.support.fixture.OrderLineItemFixture;
import kitchenpos.support.fixture.OrderTableFixture;
import kitchenpos.support.fixture.ProductFixture;
import kitchenpos.support.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;


class TableServiceTest extends ServiceTestEnvironment {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 추가할 수 있다.")
    void create() {
        // given
        final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(0, true);

        // when
        final OrderTable actual = tableService.create(orderTableCreateRequest);

        // then
        assertThat(actual).extracting("id")
                .isNotNull();
    }

    @Test
    @DisplayName("등록된 테이블을 조회할 수 있다.")
    void list() {
        // given
        final OrderTable orderTable = OrderTableFixture.createDefaultWithoutId();
        final OrderTable saved = serviceDependencies.save(orderTable);

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(saved);
    }

    @Test
    @DisplayName("특정 주문 테이블을 빈 테이블로 수정할 수 있다.")
    void changeEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 10);
        final OrderTable savedTable = serviceDependencies.save(orderTable);

        // when
        final OrderTable actual = tableService.changeEmpty(savedTable.getId(), new OrderTableSetEmptyRequest(true));

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 특정 주문 테이블을 빈 테이블로 수정할 수 없다.")
    void changeEmpty_notExists() {
        // given
        final Long notExistsId = Long.MAX_VALUE;

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(notExistsId, new OrderTableSetEmptyRequest(true)))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 속한 특정 주문 테이블은 빈 테이블로 수정할 수 없다.")
    void changeEmpty_exceptionHasTableGroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2);
        final TableGroup savedTableGroup = serviceDependencies.save(tableGroup);
        savedTable1.setTableGroupId(savedTableGroup.getId());
        serviceDependencies.save(savedTable1);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedTable1.getId(), new OrderTableSetEmptyRequest(true)))
                .isExactlyInstanceOf(TableGroupNullException.class);
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문 테이블의 주문 상태가 조리나 식사 상태면 안된다.")
    void changeEmpty_exceptionOrderStatusNotCompleted(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(false, 1);
        final OrderTable savedTable = serviceDependencies.save(orderTable1);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = serviceDependencies.save(menuGroup1);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = serviceDependencies.save(product1);
        final Product savedProduct2 = serviceDependencies.save(product2);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1.getId(), 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = serviceDependencies.save(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixture.create(savedMenu.getId());
        final Order order = OrderFixture.create(savedTable, orderStatus, orderLineItem);
        serviceDependencies.save(order);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTableSetEmptyRequest(true)))
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 수정할 수 있다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 1);
        final OrderTable saved = serviceDependencies.save(orderTable);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(saved.getId(),
                new OrderTableGuestNumberRequest(10));

        // then
        assertThat(actual).extracting("numberOfGuests")
                .isEqualTo(10);
    }

    @Test
    @DisplayName("특정 주문 테이블의 손님 수를 수정할 때 특정 주문 테이블이 존재하지 않으면 안된다.")
    void changeNumberOfGuests_exceptionWhenOrderTableNotExists() {
        // given
        final Long notExistsId = Long.MAX_VALUE;

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistsId,
                new OrderTableGuestNumberRequest(10)))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 0보다 작은 수로 수정할 수 없다.")
    void changeNumberOfGuests_exceptionWhenGuestCountNegative() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 1);
        final OrderTable saved = serviceDependencies.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(),
                new OrderTableGuestNumberRequest(-1)))
                .isExactlyInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    @DisplayName("비어있는 특정 주문 테이블의 방문한 손님 수를 수정할 수 없다.")
    void changeNumberOfGuests_exceptionWhenOrderTableIsEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(true, 1);
        final OrderTable saved = serviceDependencies.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(),
                new OrderTableGuestNumberRequest(10)))
                .isExactlyInstanceOf(EmptyOrderTableException.class);
    }
}
