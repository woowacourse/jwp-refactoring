package kitchenpos.table.application;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupServiceTest extends ApplicationTestConfig {

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(
                orderTableRepository,
                tableGroupRepository,
                tableGroupValidator
        );
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("새로운 단체 지정 등록")
    @Nested
    class CreateTableGroupNestedTest {
        @DisplayName("[SUCCESS] 단체 지정을 생성한다.")
        @Test
        void success_create() {
            // given
            final List<OrderTable> savedOrderTables = List.of(
                    orderTableRepository.save(OrderTable.withoutTableGroup(5, true)),
                    orderTableRepository.save(OrderTable.withoutTableGroup(10, true))
            );

            // when
            final List<Long> savedOrderTableIds = collectOrderTableIds(savedOrderTables);
            final TableGroupCreateRequest request = new TableGroupCreateRequest(savedOrderTableIds);
            final TableGroupResponse actual = tableGroupService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getCreatedDate()).isBefore(LocalDateTime.now());
                softly.assertThat(actual.getOrderTables())
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(OrderTableResponse.from(savedOrderTables));
            });
        }

        @DisplayName("[EXCEPTION] 주문 테이블 수가 2 이하 일 경우 예외가 발생한다.")
        @ParameterizedTest
        @MethodSource("getWrongOrderTables")
        void throwException_when_orderTablesHasSizeLessThan2(final List<OrderTable> notEnoughOrderTables) {
            // given
            final List<OrderTable> savedOrderTables = notEnoughOrderTables.stream()
                    .map(orderTableRepository::save)
                    .collect(Collectors.toList());
            final List<Long> savedOrderTableIds = collectOrderTableIds(savedOrderTables);
            final TableGroupCreateRequest request = new TableGroupCreateRequest(savedOrderTableIds);

            // expect
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Stream<Arguments> getWrongOrderTables() {
            return Stream.of(
                    Arguments.arguments(List.of()),
                    Arguments.arguments(List.of(
                            OrderTable.withoutTableGroup(5, true)
                    ))
            );
        }

        @DisplayName("[EXCEPTION] 주문 테이블 수가 실제 테이블 수와 일치하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderTablesSize_isNotEqualTo_findOrderTablesSize() {
            // given
            final OrderTable unsavedOrderTable = OrderTable.withoutTableGroup(5, false);
            final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(10, false));

            final List<Long> savedOrderTableIds = collectOrderTableIds(List.of(unsavedOrderTable, savedOrderTable));
            final TableGroupCreateRequest request = new TableGroupCreateRequest(savedOrderTableIds);

            // expect
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 비어있는 상태가 아닌 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderTable_isNotEmpty() {
            // given
            final OrderTable savedOrderTableEmpty = orderTableRepository.save(OrderTable.withoutTableGroup(10, false));

            // when
            final OrderTable savedOrderTableNotEmpty = orderTableRepository.save(OrderTable.withoutTableGroup(5, false));

            final List<Long> savedOrderTableIds = collectOrderTableIds(List.of(savedOrderTableEmpty, savedOrderTableNotEmpty));
            final TableGroupCreateRequest request = new TableGroupCreateRequest(savedOrderTableIds);

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("단체 지정 해제")
    @Nested
    class UngroupNestedTest {

        @DisplayName("[SUCCESS] 단체 지정을 해제한다.")
        @Test
        void success_ungroup() {
            // given
            final List<OrderTable> savedOrderTables = List.of(
                    orderTableRepository.save(OrderTable.withoutTableGroup(5, true)),
                    orderTableRepository.save(OrderTable.withoutTableGroup(10, true))
            );

            final List<Long> savedOrderTableIds = collectOrderTableIds(savedOrderTables);
            final TableGroupCreateRequest request = new TableGroupCreateRequest(savedOrderTableIds);
            final TableGroupResponse savedTableGroup = tableGroupService.create(request);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            final List<OrderTable> actual = orderTableRepository.findAllByIdIn(savedOrderTableIds);

            assertThat(actual).usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(List.of(
                            OrderTable.withoutTableGroup(5, false),
                            OrderTable.withoutTableGroup(10, false)
                    ));
        }

        @DisplayName("[EXCEPTION] 주문 테이블의 주문 중에서 단 하나라도 주문 상태가 COMPLETION 이 아닐 경우 예외가 발생한다.")
        @ParameterizedTest
        @MethodSource("getOrderStatusWithoutCompletion")
        void throwException_when_orderStatus_isCookingOrMeal(final OrderStatus orderStatus) {
            // given
            final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000")));
            final Menu savedMenu = createMenu(savedProduct);
            final OrderTable savedOrderTableWithFiveGuests = createOrder(orderStatus, savedMenu, 5);
            final OrderTable savedOrderTableWithTenGuests = createOrder(orderStatus, savedMenu, 10);

            final List<Long> savedOrderTableIds = collectOrderTableIds(List.of(savedOrderTableWithTenGuests, savedOrderTableWithFiveGuests));
            final TableGroupCreateRequest request = new TableGroupCreateRequest(savedOrderTableIds);
            final TableGroupResponse savedTableGroup = tableGroupService.create(request);

            // expect
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Stream<Arguments> getOrderStatusWithoutCompletion() {
            return Arrays.stream(OrderStatus.values())
                    .filter(orderStatus -> orderStatus != OrderStatus.COMPLETION)
                    .map(Arguments::arguments);
        }

        private OrderTable createOrder(final OrderStatus orderStatus, final Menu menu, final int numberOfGuests) {
            final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(numberOfGuests, true));
            final OrderLineItem orderLineItem = OrderLineItem.withoutOrder(menu.getId(), new Quantity(1));
            final Order savedOrder = orderRepository.save(Order.ofEmptyOrderLineItems(savedOrderTable.getId()));
            savedOrder.addOrderLineItems(List.of(orderLineItem));
            savedOrder.changeOrderStatus(orderStatus);

            return savedOrderTable;
        }

        private Menu createMenu(final Product savedProduct) {
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
            final Menu savedMenu = menuRepository.save(
                    Menu.withEmptyMenuProducts(
                            new Name("테스트용 메뉴명"),
                            Price.ZERO,
                            savedMenuGroup
                    )
            );
            final MenuProduct menuProduct = MenuProduct.withoutMenu(savedProduct, new Quantity(10));
            savedMenu.addMenuProducts(List.of(menuProduct));

            return savedMenu;
        }

    }

    private List<Long> collectOrderTableIds(final List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
