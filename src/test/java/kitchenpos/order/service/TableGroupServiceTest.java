package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.service.dto.OrderStatusRequest;
import kitchenpos.order.service.dto.TableGroupRequest;
import kitchenpos.menu.service.dto.MenuGroupResponse;
import kitchenpos.menu.service.dto.MenuResponse;
import kitchenpos.order.service.dto.OrderResponse;
import kitchenpos.product.service.dto.ProductResponse;
import kitchenpos.order.service.dto.TableGroupResponse;
import kitchenpos.order.service.dto.TableResponse;
import kitchenpos.menu.service.MenuService;
import kitchenpos.menu.service.MenuGroupService;
import kitchenpos.order.service.OrderService;
import kitchenpos.product.service.ProductService;
import kitchenpos.supports.IntegrationTest;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.OrderFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.ProductFixture;
import kitchenpos.supports.TableGroupFixture;
import kitchenpos.order.service.TableService;
import kitchenpos.order.service.TableGroupService;
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
            final TableResponse orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final TableResponse orderTable2 = tableService.create(OrderTableFixture.createEmpty());

            final TableGroupRequest tableGroup = TableGroupFixture.from(orderTable1, orderTable2);

            // when
            final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(savedTableGroup.getId()).isPositive();
        }

        @DisplayName("주문 테이블 목록이 비어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderTableEmpty() {
            // given
            final TableGroupRequest tableGroup = TableGroupFixture.from();

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 개수 2개 이상부터 단체 지정 가능합니다.");
        }

        @DisplayName("주문 테이블 목록의 사이즈가 2 미만이면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderTableListSizeIsLowerThanTwo() {
            // given
            final TableResponse orderTable = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup = TableGroupFixture.from(orderTable);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 개수 2개 이상부터 단체 지정 가능합니다.");
        }

        @DisplayName("존재하지 않는 주문 테이블이 들어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsInvalidOrderTable() {
            // given
            final TableResponse orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup = TableGroupFixture.fromIds(orderTable1.getId(), INVALID_ID);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블 또는 중복 테이블이 포함되어 있습니다.");
        }

        @DisplayName("중복되는 주문 테이블이 들어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsDuplicatedOrderTable() {
            // given
            final TableResponse orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup = TableGroupFixture.fromIds(orderTable1.getId(), orderTable1.getId());

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블 또는 중복 테이블이 포함되어 있습니다.");
        }

        @DisplayName("비어있지 않은 테이블이 포함되어 있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsNotEmptyOrderTable() {
            // given
            final TableResponse orderTable1 = tableService.create(OrderTableFixture.createNotEmpty());
            final TableResponse orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup = TableGroupFixture.from(orderTable1, orderTable2);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정이 불가능한 테이블이 포함되어 있습니다.");
        }

        @DisplayName("이미 단체 지정이 된 테이블이 포함되어 있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsAlreadyTableGroupingOrderTable() {
            // given
            final TableResponse orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final TableResponse orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup1 = TableGroupFixture.from(orderTable1, orderTable2);
            tableGroupService.create(tableGroup1);

            final TableResponse orderTable3 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup2 = TableGroupFixture.from(orderTable1, orderTable3);

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정이 불가능한 테이블이 포함되어 있습니다.");
        }
    }

    @Nested
    @DisplayName("단체 지정을 해제할 때")
    class Ungroup {

        @DisplayName("정상적으로 해제할 수 있다")
        @Test
        void success() {
            // given
            final TableResponse orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final TableResponse orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupRequest tableGroup = TableGroupFixture.from(orderTable1, orderTable2);
            final TableGroupResponse response = tableGroupService.create(tableGroup);

            // then
            assertThatCode(() -> tableGroupService.ungroup(response.getId()))
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

            final TableResponse orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final TableResponse orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroupResponse tableGroup = tableGroupService.create(
                    TableGroupFixture.from(orderTable1, orderTable2));

            // orderTable1에 대한 주문 상태 -> 계산 미완료
            final OrderResponse savedOrder1 = orderService.create(OrderFixture.of(menu.getId(), orderTable1.getId()));
            final OrderStatusRequest change1 = new OrderStatusRequest(orderStatus);
            orderService.changeOrderStatus(savedOrder1.getId(), change1);

            // orderTable2에 대한 주문 상태 -> 계산 완료
            final OrderResponse savedOrder2 = orderService.create(OrderFixture.of(menu.getId(), orderTable1.getId()));
            final OrderStatusRequest change2 = OrderFixture.createCompletion();
            orderService.changeOrderStatus(savedOrder2.getId(), change2);

            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("계산 완료되지 않은 테이블이 남아있어 단체 지정 해제가 불가능합니다.");
        }
    }
}
