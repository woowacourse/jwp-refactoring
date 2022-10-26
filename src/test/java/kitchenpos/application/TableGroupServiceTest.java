package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixtures.후라이드치킨;
import static kitchenpos.application.fixture.MenuGroupFixtures.한마리메뉴;
import static kitchenpos.application.fixture.MenuProductFixtures.generateMenuProduct;
import static kitchenpos.application.fixture.OrderFixtures.generateOrder;
import static kitchenpos.application.fixture.OrderLineItemFixtures.generateOrderLineItem;
import static kitchenpos.application.fixture.OrderTableFixtures.*;
import static kitchenpos.application.fixture.ProductFixtures.후라이드;
import static kitchenpos.application.fixture.TableGroupFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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

    @Test
    void tableGroup을_생성한다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());

        TableGroup actual = tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번)));

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @Test
    void orderTables가_비어있으면_예외를_던진다() {
        TableGroup tableGroup = generateTableGroup(List.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTables의_사이즈가_2미만인_경우_예외를_던진다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());

        assertThatThrownBy(() -> tableGroupService.create(generateTableGroup(List.of(테이블_1번))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderGroup이_가진_orderTables의_사이즈와_저장된_orderTables의_사이즈가_다른_경우_예외를_던진다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());
        OrderTable 테이블_3번 = 테이블_3번();

        assertThatThrownBy(() -> tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번, 테이블_3번))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTables_중_비어있지_않은_table이_존재하는_경우_예외를_던진다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());
        OrderTable 비어있지_않은_테이블 = tableService.create(generateOrderTable(0, false));

        assertThatThrownBy(() -> tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번, 비어있지_않은_테이블))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_orderTables_중_tableGroupId가_null이_아닌_경우_예외를_던진다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());

        tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번)));

        OrderTable 테이블_3번 = tableService.create(테이블_3번());

        assertThatThrownBy(() -> tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번, 테이블_3번))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void tableGroup을_해제한다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());
        TableGroup tableGroup = tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번)));

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> actual = tableService.list();

        assertAll(() -> {
            assertThat(actual.get(0).getTableGroupId()).isNull();
            assertThat(actual.get(0).isEmpty()).isFalse();
            assertThat(actual.get(1).getTableGroupId()).isNull();
            assertThat(actual.get(1).isEmpty()).isFalse();
        });
    }

    @Test
    void orderTables의_orderStatus가_COOKING_MEAL인_경우_예외를_던진다() {
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        OrderTable 테이블_2번 = tableService.create(테이블_2번());
        TableGroup tableGroup = tableGroupService.create(generateTableGroup(List.of(테이블_1번, 테이블_2번)));

        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);
        orderService.create(generateOrder(테이블_1번.getId(), List.of(orderLineItem)));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
