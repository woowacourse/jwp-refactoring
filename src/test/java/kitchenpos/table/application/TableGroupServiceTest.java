package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.SingleOrderTableCreateRequest;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable savedOrderTable1;

    private OrderTable savedOrderTable2;

    private Menu menu;

    @BeforeEach
    void setUp() {
        final OrderTable orderTable1 = new OrderTable(null, 10, true);
        final OrderTable orderTable2 = new OrderTable(null, 15, true);
        savedOrderTable1 = orderTableRepository.save(orderTable1);
        savedOrderTable2 = orderTableRepository.save(orderTable2);

        final Product product = productRepository.save(Product.of("후라이드", BigDecimal.valueOf(15_000L)));
        final MenuProduct menuProduct = new MenuProduct(null, product, 1L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드 세트"));
        final Menu newMenu = Menu.of("치킨", BigDecimal.valueOf(15_000L), menuGroup.getId(), List.of(menuProduct));
        menu = menuRepository.save(newMenu);
    }

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 정상_요청() {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    new SingleOrderTableCreateRequest(savedOrderTable1.getId()),
                    new SingleOrderTableCreateRequest(savedOrderTable2.getId())
            );

            // when
            TableGroupResponse response = tableGroupService.create(request);

            // then
            assertThat(response.getOrderTables())
                    .extracting(OrderTableResponse::getId)
                    .contains(savedOrderTable1.getId(), savedOrderTable2.getId());
        }

        @Test
        void 요청의_주문_테이블이_2개_미만이면_예외_발생() {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    new SingleOrderTableCreateRequest(savedOrderTable1.getId())
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청의_주문_테이블이_존재하지_않으면_예외_발생() {
            // given
            long invalidOrderTableId = -1L;
            TableGroupCreateRequest request = createTableGroup(
                    new SingleOrderTableCreateRequest(invalidOrderTableId)
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 요청의_주문_테이블이_비어있지_않으면_예외_발생() {
            // given
            OrderTable orderTable = new OrderTable(null, 15, false);
            orderTable = orderTableRepository.save(orderTable);
            TableGroupCreateRequest request = createTableGroup(
                    new SingleOrderTableCreateRequest(orderTable.getId()),
                    new SingleOrderTableCreateRequest(savedOrderTable1.getId())
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블에_그룹이_이미_존재하면_예외_발생() {
            // given
            OrderTable orderTable1 = new OrderTable(null, 10, true);
            OrderTable orderTable2 = new OrderTable(null, 15, true);
            OrderTable savedOrderTable1 = orderTableRepository.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableRepository.save(orderTable2);
            TableGroup savedTableGroup = tableGroupRepository.save(
                    new TableGroup(LocalDateTime.now(), List.of(savedOrderTable1, savedOrderTable2)));
            savedOrderTable1.updateTableGroup(savedTableGroup);

            TableGroupCreateRequest request = createTableGroup(
                    new SingleOrderTableCreateRequest(savedOrderTable1.getId()),
                    new SingleOrderTableCreateRequest(savedOrderTable2.getId())
            );

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_삭제 {

        @Test
        void 정상_요청() {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    new SingleOrderTableCreateRequest(savedOrderTable1.getId()),
                    new SingleOrderTableCreateRequest(savedOrderTable2.getId())
            );
            TableGroupResponse response = tableGroupService.create(request);

            // when, then
            assertDoesNotThrow(
                    () -> tableGroupService.ungroup(response.getId())
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COOKING"})
        void 요청에_대한_주문의_상태가_COOKING이나_MEAL이면_예외_발생(final String orderStatus) {
            // given
            TableGroupCreateRequest request = createTableGroup(
                    new SingleOrderTableCreateRequest(savedOrderTable1.getId()),
                    new SingleOrderTableCreateRequest(savedOrderTable2.getId())
            );
            TableGroupResponse response = tableGroupService.create(request);
            Order order = createOrder(savedOrderTable1);
            order.updateOrderStatus(orderStatus);
            orderRepository.save(order);

            // when, then
            assertThatThrownBy(
                    () -> tableGroupService.ungroup(response.getId())
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private TableGroupCreateRequest createTableGroup(final SingleOrderTableCreateRequest... tableRequests) {
        return new TableGroupCreateRequest(Arrays.asList(tableRequests));
    }

    private Order createOrder(final OrderTable orderTable) {
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, menu.getId(), 1L),
                new OrderLineItem(null, menu.getId(), 1L));
        return new Order(orderTable, LocalDateTime.now(), orderLineItems);
    }
}
