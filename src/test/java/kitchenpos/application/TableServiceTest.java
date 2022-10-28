package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
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
        final OrderTable orderTable = OrderTableFixture.createDefaultWithoutId();

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(actual).extracting("id")
                        .isNotNull(),
                () -> assertThat(actual)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderTable)
        );
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
        final OrderTable actual = tableService.changeEmpty(savedTable.getId(), OrderTableFixture.create(true, 10));

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 특정 주문 테이블을 빈 테이블로 수정할 수 없다.")
    void changeEmpty_notExists() {
        // given
        final Long notExistsId = Long.MAX_VALUE;

        // when, then
        assertThatThrownBy(() ->tableService.changeEmpty(notExistsId, OrderTableFixture.create(true, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
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
        assertThatThrownBy(() ->tableService.changeEmpty(savedTable1.getId(), OrderTableFixture.create(true, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("주문 테이블의 주문 상태가 조리나 식사 상태면 안된다.")
    void changeEmpty_exceptionOrderStatusNotCompleted(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(false, 1);
        final OrderTable savedTable =  serviceDependencies.save(orderTable1);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = serviceDependencies.save(menuGroup1);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = serviceDependencies.save(product1);
        final Product savedProduct2 = serviceDependencies.save(product2);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1.getId(), 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = serviceDependencies.save(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixture.create(savedMenu);
        final Order order = OrderFixture.create(savedTable, orderStatus, orderLineItem);
        serviceDependencies.save(order);

        // when, then
        assertThatThrownBy(() ->tableService.changeEmpty(savedTable.getId(), OrderTableFixture.create(true, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 수정할 수 있다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 1);
        final OrderTable saved = serviceDependencies.save(orderTable);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(saved.getId(),
                OrderTableFixture.create(false, 10));

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
                OrderTableFixture.create(false, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 0보다 작은 수로 수정할 수 없다.")
    void changeNumberOfGuests_exceptionWhenGuestCountNegative() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 1);
        final OrderTable saved = serviceDependencies.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(),
                OrderTableFixture.create(false, -1)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 특정 주문 테이블의 방문한 손님 수를 수정할 수 없다.")
    void changeNumberOfGuests_exceptionWhenOrderTableIsEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(true, 1);
        final OrderTable saved = serviceDependencies.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(),
                OrderTableFixture.create(false, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
