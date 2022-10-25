package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static kitchenpos.fixture.MenuFixtures.후라이드치킨;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴;
import static kitchenpos.fixture.MenuProductFixtures.createMenuProduct;
import static kitchenpos.fixture.OrderFixtures.createOrder;
import static kitchenpos.fixture.OrderLineItemFixtures.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixtures.createOrderTable;
import static kitchenpos.fixture.OrderTableFixtures.테이블_1번;
import static kitchenpos.fixture.OrderTableFixtures.테이블_2번;
import static kitchenpos.fixture.OrderTableFixtures.테이블_3번;
import static kitchenpos.fixture.ProductFixtures.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    void createOrderTableSuccess() {
        OrderTable 테이블_1번 = 테이블_1번();

        OrderTable actual = tableService.create(테이블_1번);

        assertAll(
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(테이블_1번.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @DisplayName("orderTable list를 조회한다.")
    @Test
    void findAllOrderTable() {
        tableService.create(테이블_1번());
        tableService.create(테이블_2번());
        tableService.create(테이블_3번());

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(3);
    }

    @DisplayName("orderTable의 epmty를 변경한다.")
    @Test
    void changeOrderTableEmpty() {
        // 메뉴 설정
        Product 후라이드 = productService.create(후라이드());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        Menu 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId());
        메뉴_후라이드치킨.setMenuProducts(List.of(createMenuProduct(후라이드.getId(), 1)));
        메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨);

        // 테이블 설정
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        tableService.changeEmpty(테이블_1번.getId(), createOrderTable(0, false));

        // 주문
        OrderLineItem orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);
        Order order = orderService.create(createOrder(테이블_1번.getId(), List.of(orderLineItem)));

        // 계산완료
        Order changedOrder = createOrder(테이블_1번.getId(), OrderStatus.COMPLETION, List.of(orderLineItem));
        orderService.changeOrderStatus(order.getId(), changedOrder);

        // empty 변경
        OrderTable actual = tableService.changeEmpty(테이블_1번.getId(), createOrderTable(0, false));

        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isEqualTo(테이블_1번.getNumberOfGuests());
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @DisplayName("numberOfGuests가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void numberOfGuestsUnderZero(final int numberOfGuests) {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());

        OrderTable actual = createOrderTable(numberOfGuests, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_1번.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 orderTable이 비어있는 경우 예외를 던진다.")
    @Test
    void savedOrderTableIsEmpty() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());

        OrderTable actual = createOrderTable(2, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_1번.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
