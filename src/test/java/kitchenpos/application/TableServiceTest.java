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
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("주문 테이블을 추가할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable = OrderTableFixture.createDefaultWithoutId();

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderTable),
                () -> assertThat(actual).extracting("id")
                        .isNotNull()
        );
    }

    @Test
    @DisplayName("등록된 테이블을 조회할 수 있다.")
    void list() {
        // given
        final OrderTable orderTable = OrderTableFixture.createDefaultWithoutId();
        final OrderTable saved = tableService.create(orderTable);

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("id")
                .contains(saved);
    }

    @Test
    @DisplayName("특정 주문 테이블을 빈 테이블로 수정할 수 있다.")
    void changeEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 10);
        final OrderTable savedTable = tableService.create(orderTable);

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
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2);
        tableGroupService.create(tableGroup);

        // when, then
        assertThatThrownBy(() ->tableService.changeEmpty(savedTable1.getId(), OrderTableFixture.create(true, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 주문 상태가 조리나 식사 상태면 안된다.")
    void changeEmpty_exceptionOrderStatusNotCompleted() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(false, 1);
        final OrderTable savedTable = tableService.create(orderTable1);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = menuService.create(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixture.create(savedMenu);
        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem);
        orderService.create(order);

        // when, then
        assertThatThrownBy(() ->tableService.changeEmpty(savedTable.getId(), OrderTableFixture.create(true, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 수정할 수 있다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 1);
        final OrderTable saved = tableService.create(orderTable);

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
        final OrderTable saved = tableService.create(orderTable);

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
        final OrderTable saved = tableService.create(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(),
                OrderTableFixture.create(false, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
