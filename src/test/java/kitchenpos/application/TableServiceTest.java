package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixtures.후라이드치킨;
import static kitchenpos.application.fixture.MenuGroupFixtures.한마리메뉴;
import static kitchenpos.application.fixture.MenuProductFixtures.generateMenuProduct;
import static kitchenpos.application.fixture.OrderFixtures.generateOrder;
import static kitchenpos.application.fixture.OrderLineItemFixtures.generateOrderLineItem;
import static kitchenpos.application.fixture.OrderTableFixtures.*;
import static kitchenpos.application.fixture.ProductFixtures.후라이드;
import static kitchenpos.application.fixture.TableGroupFixtures.generateTableGroup;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class TableServiceTest {

    private final TableService tableService;
    private final TableGroupService tableGroupService;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;
    private final OrderService orderService;

    @Autowired
    public TableServiceTest(final TableService tableService, final TableGroupService tableGroupService,
                            final MenuGroupService menuGroupService, final ProductService productService,
                            final MenuService menuService, final OrderService orderService) {
        this.tableService = tableService;
        this.tableGroupService = tableGroupService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
        this.orderService = orderService;
    }

    @DisplayName("orderTable을 생성한다.")
    @Test
    void orderTable을_생성한다() {
        OrderTable 테이블_1번 = 테이블_1번();

        OrderTable actual = tableService.create(테이블_1번);

        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isEqualTo(테이블_1번.getNumberOfGuests());
            assertThat(actual.isEmpty()).isTrue();
        });
    }

    @DisplayName("orderTable list를 조회한다.")
    @Test
    void orderTable_list를_조회한다() {
        tableService.create(테이블_1번());
        tableService.create(테이블_2번());
        tableService.create(테이블_3번());
        tableService.create(테이블_4번());
        tableService.create(테이블_5번());

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(5);
    }

    @DisplayName("orderTable의 epmty를 변경한다.")
    @Test
    void orderTable의_empty를_변경한다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        tableService.changeEmpty(테이블_1번.getId(), generateOrderTable(0, false));

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);
        Order order = orderService.create(generateOrder(테이블_1번.getId(), List.of(orderLineItem)));

        Order changedOrder = generateOrder(테이블_1번.getId(), OrderStatus.COMPLETION, List.of(orderLineItem));
        orderService.changeOrderStatus(order.getId(), changedOrder);

        OrderTable actual = tableService.changeEmpty(테이블_1번.getId(), generateOrderTable(0, false));

        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isEqualTo(테이블_1번.getNumberOfGuests());
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @DisplayName("tableGroupId가 null이 아닌 경우 예외를 던진다.")
    @Test
    void tableGroupId가_null이_아닌_경우_예외를_던진다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());

        tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번)));

        assertThatThrownBy(() -> tableService.changeEmpty(테이블_1번.getId(), generateOrderTable(0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable에 속한 order 중 COOKING, MEAL이 존재하는 경우 예외를 던진다.")
    @Test
    void orderTable에_속한_order_중_COOKING_MEAL이_존재하는_경우_예외를_던진다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        tableService.changeEmpty(테이블_1번.getId(), generateOrderTable(0, false));

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);
        Order order = orderService.create(generateOrder(테이블_1번.getId(), List.of(orderLineItem)));

        Order changedOrder = generateOrder(테이블_1번.getId(), OrderStatus.MEAL, List.of(orderLineItem));
        orderService.changeOrderStatus(order.getId(), changedOrder);

        assertThatThrownBy(() -> tableService.changeEmpty(테이블_1번.getId(), generateOrderTable(0, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable의 chagneNumberOfGuests를 변경한다.")
    @Test
    void orderTable의_changeNumberOfGuests를_변경한다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());
        tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번)));

        OrderTable actual = tableService.changeNumberOfGuests(테이블_1번.getId(), generateOrderTable(1, false));

        assertAll(() -> {
            assertThat(actual.isEmpty()).isFalse();
            assertThat(actual.getNumberOfGuests()).isEqualTo(1);
        });
    }

    @DisplayName("numberOfGuests가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void numberOfGuests가_0미만인_경우_예외를_던진다(final int numberOfGuests) {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());

        OrderTable actual = generateOrderTable(numberOfGuests, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_1번.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 orderTable이 비어있는 경우 예외를 던진다.")
    @Test
    void 저장된_orderTable이_비어있는_경우_예외를_던진다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());

        OrderTable actual = generateOrderTable(2, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_1번.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
