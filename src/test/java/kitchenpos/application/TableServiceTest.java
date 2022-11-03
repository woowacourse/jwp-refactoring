package kitchenpos.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixtures.createMenu;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴;
import static kitchenpos.fixture.MenuProductFixtures.createMenuProduct;
import static kitchenpos.fixture.OrderFixtures.createOrder;
import static kitchenpos.fixture.OrderLineItemFixtures.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixtures.createOrderTableUpdateEmptyRequest;
import static kitchenpos.fixture.OrderTableFixtures.createOrderTableUpdateNumberOfGuestsRequest;
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
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;
    private final OrderService orderService;

    @Autowired
    public TableServiceTest(final TableService tableService, final MenuGroupService menuGroupService,
                            final ProductService productService, final MenuService menuService,
                            final OrderService orderService) {
        this.tableService = tableService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
        this.orderService = orderService;
    }

    @DisplayName("orderTable을 생성한다.")
    @Test
    void createOrderTableSuccess() {
        OrderTableCreateRequest 테이블_1번 = 테이블_1번();

        OrderTableResponse actual = tableService.create(테이블_1번);

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

        List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(3);
    }

    @DisplayName("orderTable의 empty를 변경한다.")
    @Test
    void changeOrderTableEmpty() {
        // 메뉴 설정
        ProductResponse 후라이드 = productService.create(후라이드());
        MenuGroupResponse 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuCreateRequest request = createMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(),
                List.of(createMenuProduct(후라이드.getId(), 1)));
        MenuResponse 메뉴_후라이드치킨 = menuService.create(request);

        // 테이블 설정
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());
        tableService.changeEmpty(테이블_1번.getId(), createOrderTableUpdateEmptyRequest(false));

        // 주문
        OrderLineItemCreateRequest orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);
        OrderResponse order = orderService.create(createOrder(테이블_1번.getId(), List.of(orderLineItem)));

        // 계산완료
        OrderCreateRequest changedOrder = createOrder(테이블_1번.getId(), OrderStatus.COMPLETION.name(),
                List.of(orderLineItem));
        orderService.changeOrderStatus(order.getId(), changedOrder);

        // empty 변경
        OrderTableResponse actual = tableService.changeEmpty(테이블_1번.getId(), createOrderTableUpdateEmptyRequest(false));

        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isEqualTo(테이블_1번.getNumberOfGuests());
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @DisplayName("numberOfGuests가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void numberOfGuestsUnderZero(final int numberOfGuests) {
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());

        OrderTableUpdateNumberOfGuestsRequest actual = createOrderTableUpdateNumberOfGuestsRequest(numberOfGuests);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_1번.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 orderTable이 비어있는 경우 예외를 던진다.")
    @Test
    void savedOrderTableIsEmpty() {
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());


        OrderTableUpdateNumberOfGuestsRequest actual = createOrderTableUpdateNumberOfGuestsRequest(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블_1번.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
