package kitchenpos.application;

import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.order.application.dto.request.OrderLineItemCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableIdRequest;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.product.application.ProductService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static kitchenpos.fixture.OrderTableFixtures.테이블_1번;
import static kitchenpos.fixture.OrderTableFixtures.테이블_2번;
import static kitchenpos.fixture.ProductFixtures.후라이드;
import static kitchenpos.fixture.TableGroupFixtures.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/truncate.sql")
class TableGroupServiceTest {

    private final TableService tableService;
    private final TableGroupService tableGroupService;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;
    private final OrderService orderService;

    @Autowired
    public TableGroupServiceTest(final TableService tableService, final TableGroupService tableGroupService,
                                 final MenuGroupService menuGroupService, final ProductService productService,
                                 final MenuService menuService, final OrderService orderService) {
        this.tableService = tableService;
        this.tableGroupService = tableGroupService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
        this.orderService = orderService;
    }

    @DisplayName("tableGroup을 생성한다.")
    @Test
    void createTableGroupSuccess() {
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());
        OrderTableResponse 테이블_2번 = tableService.create(테이블_2번());
        List<OrderTableIdRequest> 테이블_그룹 = List.of(new OrderTableIdRequest(테이블_1번.getId()),
                new OrderTableIdRequest(테이블_2번.getId()));

        TableGroupResponse actual = tableGroupService.create(createTableGroup(테이블_그룹));

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @DisplayName("TableGroup이 비어있으면 예외를 던진다.")
    @Test
    void createTableGroupByEmpty() {
        TableGroupCreateRequest tableGroup = createTableGroup(List.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup이 사이즈가 2미만인 경우 예외를 던진다.")
    @Test
    void createTableGroupByOneSize() {
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());
        List<OrderTableIdRequest> 테이블_그룹 = List.of(new OrderTableIdRequest(테이블_1번.getId()));

        assertThatThrownBy(() -> tableGroupService.create(createTableGroup(테이블_그룹)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup이 가진 orderTables의 사이즈와 실제 orderTables의 사이즈가 다른 경우 예외를 던진다.")
    @Test
    void createTableGroupByRealNotSavedOrderTables() {
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());
        OrderTableResponse 테이블_2번 = tableService.create(테이블_2번());
        Long 테이블_3번_빈값 = 0L;

        List<OrderTableIdRequest> 테이블_그룹 = List.of(new OrderTableIdRequest(테이블_1번.getId()),
                new OrderTableIdRequest(테이블_2번.getId()),
                new OrderTableIdRequest(테이블_3번_빈값));

        assertThatThrownBy(() -> tableGroupService.create(createTableGroup(테이블_그룹)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("tableGroup을 해제한다.")
    @Test
    void unlockTableGroup() {
        // 테이블 그룹화
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());
        OrderTableResponse 테이블_2번 = tableService.create(테이블_2번());
        List<OrderTableIdRequest> 테이블_그룹 = List.of(new OrderTableIdRequest(테이블_1번.getId()),
                new OrderTableIdRequest(테이블_2번.getId()));
        TableGroupResponse tableGroup = tableGroupService.create(createTableGroup(테이블_그룹));

        // 해제
        tableGroupService.ungroup(tableGroup.getId());

        // 결과
        List<OrderTableResponse> actual = tableService.list();
        assertAll(
                () -> assertThat(actual.get(0).getTableGroupId()).isNull(),
                () -> assertThat(actual.get(0).isEmpty()).isFalse(),
                () -> assertThat(actual.get(1).getTableGroupId()).isNull(),
                () -> assertThat(actual.get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("orderTables의 orderStatus가 COOKING, MEAL인 경우 unlock 시 예외를 던진다.")
    @Test
    void unlockCheckingStatus() {
        // 메뉴 설정
        ProductResponse 후라이드 = productService.create(후라이드());
        MenuGroupResponse 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuCreateRequest request = createMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(),
                List.of(createMenuProduct(후라이드.getId(), 1)));
        MenuResponse 메뉴_후라이드치킨 = menuService.create(request);

        // 테이블 설정 및 그룹화
        OrderTableResponse 테이블_1번 = tableService.create(테이블_1번());
        OrderTableResponse 테이블_2번 = tableService.create(테이블_2번());
        List<OrderTableIdRequest> 테이블_그룹 = List.of(new OrderTableIdRequest(테이블_1번.getId()),
                new OrderTableIdRequest(테이블_2번.getId()));
        TableGroupResponse tableGroup = tableGroupService.create(createTableGroup(테이블_그룹));

        // 1번 테이블 주문
        OrderLineItemCreateRequest orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);
        orderService.create(createOrder(테이블_1번.getId(), List.of(orderLineItem)));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
