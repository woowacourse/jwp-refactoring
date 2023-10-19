package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.OrderFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.ProductFixture;
import kitchenpos.supports.IntegrationTest;
import kitchenpos.supports.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("테이블 서비스 테스트")
@IntegrationTest
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    @DisplayName("테이블을 생성할 때")
    class Create {

        @DisplayName("정상적으로 생성된다")
        @Test
        void success() {
            //given
            final OrderTable table = OrderTableFixture.createEmpty();

            //when
            final OrderTable savedOrderTable = tableService.create(table);

            //then
            assertThat(savedOrderTable.getId()).isPositive();
        }
    }

    @Nested
    @DisplayName("테이블의 손님 수를 변경 할 때")
    class ChangeNumberOfGuests {

        @DisplayName("정상적으로 변경된다")
        @Test
        void success() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());

            final OrderTable change = new OrderTable();
            change.setNumberOfGuests(2);
            final Long orderTableId = orderTable.getId();

            // when
            final OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTableId, change);

            // then
            assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(change.getNumberOfGuests());
        }

        @DisplayName("테이블이 존재하지 않으면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrderTable() {
            // given
            final OrderTable change = new OrderTable();
            change.setNumberOfGuests(2);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, change))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("손님 수를 0명 미만으로 변경하려 하면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidNumberOfGuests() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());

            final OrderTable change = new OrderTable();
            change.setNumberOfGuests(-1);
            final Long orderTableId = orderTable.getId();

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, change))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블을 변경하려하면 예외처리 한다")
        @Test
        void throwExceptionWhenEmptyTable() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createEmpty());

            final OrderTable change = new OrderTable();
            change.setNumberOfGuests(2);
            final Long orderTableId = orderTable.getId();

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, change))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("빈 테이블로 변경할 때")
    class ChangeEmpty {

        @DisplayName("정상적으로 변경된다")
        @Test
        void success() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());

            final OrderTable change = OrderTableFixture.createEmpty();
            final Long orderTableId = orderTable.getId();

            // when
            final OrderTable savedOrderTable = tableService.changeEmpty(orderTableId, change);

            // then
            assertThat(savedOrderTable.isEmpty()).isTrue();
        }

        @DisplayName("테이블이 존재하지 않으면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrderTable() {
            // given
            final OrderTable change = OrderTableFixture.createEmpty();

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, change))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정된 테이블을 변경하려 하면 예외처리 한다")
        @Test
        void throwExceptionWhenTableGroupExist() {
            // given
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup = TableGroupFixture.from(orderTable1, orderTable2);
            tableGroupService.create(tableGroup);

            final OrderTable change = OrderTableFixture.createEmpty();
            final Long orderTableId = orderTable1.getId();

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, change))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 계산 완료가 아니면 예외처리 한다")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void throwExceptionWhenIllegalOrderStatus(String orderStatus) {
            // given
            final Product product = productService.create(ProductFixture.create());
            final MenuGroup menuGroup = menuGroupService.create(MenuGroupFixture.create());
            final Menu menu = menuService.create(MenuFixture.of(menuGroup.getId(), List.of(product)));

            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final Order savedOrder = orderService.create(OrderFixture.of(menu.getId(), orderTable.getId()));

            // 주문 상태 변경
            final Order change = new Order();
            change.setOrderStatus(orderStatus);
            orderService.changeOrderStatus(savedOrder.getId(), change);

            final OrderTable empty = OrderTableFixture.createEmpty();
            final Long orderTableId = orderTable.getId();

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, empty))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("테이블 목록을 조회할 수 있다")
    @Test
    void findAllTables() {
        // given
        final OrderTable savedOrderTable = tableService.create(OrderTableFixture.createEmpty());

        // when
        final List<OrderTable> list = tableService.list();

        // then
        assertSoftly(softly -> {
            assertThat(list).hasSize(1);
            assertThat(list.get(0))
                    .usingRecursiveComparison()
                    .isEqualTo(savedOrderTable);
        });
    }
}
