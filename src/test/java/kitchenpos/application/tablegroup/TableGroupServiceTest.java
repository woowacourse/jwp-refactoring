package kitchenpos.application.tablegroup;

import kitchenpos.application.TableGroupService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                orderRepository,
                orderTableRepository,
                tableGroupRepository
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
                    orderTableRepository.save(new OrderTable(null, 5, true)),
                    orderTableRepository.save(new OrderTable(null, 10, true))
            );

            // when
            final TableGroup expected = new TableGroup(LocalDateTime.now(), savedOrderTables);
            final TableGroup actual = tableGroupService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getCreatedDate()).isEqualTo(expected.getCreatedDate());
                softly.assertThat(actual.getOrderTables())
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(List.of(
                                new OrderTable(actual, 5, false),
                                new OrderTable(actual, 10, false)
                        ));
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
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);

            // expect
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Stream<Arguments> getWrongOrderTables() {
            return Stream.of(
                    Arguments.arguments(List.of()),
                    Arguments.arguments(List.of(
                            new OrderTable(null, 5, true)
                    ))
            );
        }

        @DisplayName("[EXCEPTION] 주문 테이블 수가 실제 테이블 수와 일치하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderTablesSize_isNotEqualTo_findOrderTablesSize() {
            // given
            final OrderTable unsavedOrderTable = new OrderTable(null, 5, true);
            final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 10, true));

            final TableGroup tableGroup = new TableGroup(
                    LocalDateTime.now(),
                    List.of(unsavedOrderTable, savedOrderTable)
            );

            // expect
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 비어있는 상태가 아닌 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderTable_isNotEmpty() {
            // given
            final OrderTable savedOrderTableEmpty = orderTableRepository.save(new OrderTable(null, 10, true));

            // when
            final OrderTable savedOrderTableNotEmpty = orderTableRepository.save(new OrderTable(null, 5, false));
            final TableGroup tableGroup = new TableGroup(
                    LocalDateTime.now(),
                    List.of(savedOrderTableNotEmpty, savedOrderTableEmpty)
            );

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 비어있어도 단체 지정이 할당되어 있을 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderTable_hasAlreadyTableGroup() {
            // given
            final List<OrderTable> savedOrderTables = List.of(
                    orderTableRepository.save(new OrderTable(null, 5, true)),
                    orderTableRepository.save(new OrderTable(null, 10, true))
            );

            final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), savedOrderTables));
            savedTableGroup.addOrderTablesAndChangeEmptyFull(savedOrderTables);

            // when
            final TableGroup tableGroup = new TableGroup(
                    LocalDateTime.now(),
                    savedOrderTables
            );

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
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
                    orderTableRepository.save(new OrderTable(null, 5, true)),
                    orderTableRepository.save(new OrderTable(null, 10, true))
            );

            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            final List<Long> savedOrderTableIds = savedOrderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());
            final List<OrderTable> actual = orderTableRepository.findAllByIdIn(savedOrderTableIds);

            assertThat(actual).usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(List.of(
                            new OrderTable(null, 5, false),
                            new OrderTable(null, 10, false)
                    ));
        }

        @DisplayName("[EXCEPTION] 주문 테이블의 주문 중에서 단 하나라도 주문 상태가 COMPLETION 이 아닐 경우 예외가 발생한다.")
        @ParameterizedTest
        @MethodSource("getOrderStatusWithoutCompletion")
        void throwException_when_orderStatus_isCookingOrMeal(final OrderStatus orderStatus) {
            // given
            final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000")));
            final Menu savedMenu = createMenu(savedProduct);
            final OrderTable savedOrderTableWithFiveGuests = createOrder(orderStatus, savedMenu, 5);
            final OrderTable savedOrderTableWithTenGuests = createOrder(orderStatus, savedMenu, 10);
            final TableGroup savedTableGroup = tableGroupService.create(
                    new TableGroup(LocalDateTime.now(), List.of(savedOrderTableWithFiveGuests, savedOrderTableWithTenGuests))
            );

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
            final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, numberOfGuests, true));
            final OrderLineItem orderLineItem = new OrderLineItem(null, menu, 1);
            final Order savedOrder = orderRepository.save(
                    new Order(
                            savedOrderTable,
                            orderStatus.name(),
                            LocalDateTime.now(),
                            new ArrayList<>()
                    )
            );
            savedOrder.addOrderLineItems(List.of(orderLineItem));

            return savedOrderTable;
        }

        private Menu createMenu(final Product savedProduct) {
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
            final Menu savedMenu = menuRepository.save(
                    new Menu(
                            new Name("테스트용 메뉴명"),
                            new Price("10000"),
                            savedMenuGroup,
                            new ArrayList<>()
                    )
            );
            final MenuProduct menuProduct = new MenuProduct(null, savedProduct, 10);
            savedMenu.addMenuProducts(List.of(menuProduct));

            return savedMenu;
        }
    }
}
