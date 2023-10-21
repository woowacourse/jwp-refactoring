package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.supports.IntegrationTest;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.OrderFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.ProductFixture;
import kitchenpos.supports.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("단체 지정 서비스 테스트")
@IntegrationTest
class TableGroupServiceTest {

    private static final long INVALID_ID = -1L;

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private OrderService orderService;

    @Nested
    @DisplayName("단체 지정을 할 때")
    class Create {

        @DisplayName("정상적으로 단체 지정된다")
        @Test
        void success() {
            // given
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());

            final TableGroup tableGroup = TableGroupFixture.from(orderTable1, orderTable2);

            // when
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(savedTableGroup.getId()).isPositive();
        }

        @DisplayName("주문 테이블 목록이 비어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderTableEmpty() {
            // given
            final TableGroup tableGroup = TableGroupFixture.from();

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블 목록의 사이즈가 2 미만이면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderTableListSizeIsLowerThanTwo() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup = TableGroupFixture.from(orderTable);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블이 들어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsInvalidOrderTable() {
            // given
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(INVALID_ID);
            final TableGroup tableGroup = TableGroupFixture.from(orderTable1, orderTable2);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있지 않은 테이블이 포함되어 있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsNotEmptyOrderTable() {
            // given
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createNotEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup = TableGroupFixture.from(orderTable1, orderTable2);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체 지정이 된 테이블이 포함되어 있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsAlreadyTableGroupingOrderTable() {
            // given
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup1 = TableGroupFixture.from(orderTable1, orderTable2);
            tableGroupService.create(tableGroup1);

            final OrderTable orderTable3 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup2 = TableGroupFixture.from(orderTable1, orderTable3);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("단체 지정을 해제할 때")
    class Ungroup {

        @DisplayName("정상적으로 해제할 수 있다")
        @Test
        void success() {
            // given
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup = TableGroupFixture.from(orderTable1, orderTable2);

            // then
            assertThatCode(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .doesNotThrowAnyException();
        }

        @DisplayName("주문 상태가 계산 완료가 아닌 테이블이 포함되어 있다면 예외처리 한다")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void throwExceptionWhenContainsAlreadyTableGroupingOrderTable(String orderStatus) {
            // given
            final ProductResponse product = productService.create(ProductFixture.create());
            final MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupFixture.create());
            final MenuResponse menu = menuService.create(MenuFixture.of(menuGroup.getId(), List.of(product)));

            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup = tableGroupService.create(TableGroupFixture.from(orderTable1, orderTable2));

            // orderTable1에 대한 주문 상태 -> 계산 미완료
            final Order savedOrder1 = orderService.create(OrderFixture.of(menu.getId(), orderTable1.getId()));
            final Order change1 = new Order();
            change1.setOrderStatus(orderStatus);
            orderService.changeOrderStatus(savedOrder1.getId(), change1);

            // orderTable2에 대한 주문 상태 -> 계산 완료
            final Order savedOrder2 = orderService.create(OrderFixture.of(menu.getId(), orderTable1.getId()));
            final Order change2 = new Order();
            change2.setOrderStatus("COMPLETION");
            orderService.changeOrderStatus(savedOrder2.getId(), change2);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
