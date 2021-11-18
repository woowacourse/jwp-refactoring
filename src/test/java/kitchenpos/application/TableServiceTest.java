package kitchenpos.application;

import kitchenpos.SpringBootTestSupport;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.OrderTableResponse;
import kitchenpos.ui.dto.OrderTableEmptyRequest;
import kitchenpos.ui.dto.OrderTableGuestRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static kitchenpos.MenuFixture.createMenu1;
import static kitchenpos.MenuFixture.createMenuGroup1;
import static kitchenpos.OrderFixture.createOrder;
import static kitchenpos.OrderFixture.createOrderLineItem;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableServiceTest extends SpringBootTestSupport {

    @Autowired
    private TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        final OrderTableRequest request = new OrderTableRequest(10, true);

        final OrderTableResponse actual = tableService.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isEqualTo(request.isEmpty())
        );
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        final OrderTable orderTable1 = save(createOrderTable());
        final OrderTable orderTable2 = save(createOrderTable());

        final List<OrderTableResponse> actual = tableService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0).getId()).isEqualTo(orderTable1.getId()),
                () -> assertThat(actual.get(1).getId()).isEqualTo(orderTable2.getId())
        );
    }

    @DisplayName("빈 테이블로의 변경은")
    @Nested
    class ChangeEmpty extends SpringBootTestSupport {

        private OrderTableEmptyRequest request;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            request = new OrderTableEmptyRequest(true);
            orderTable = save(createOrderTable());
        }

        @DisplayName("존재하지 않는 주문 테이블은 변경할 수 없다.")
        @Test
        void changeEmptyIfNotExist() {
            assertThatThrownBy(() -> tableService.changeEmpty(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정에 속해있는 주문 테이블은 변경할 수 없다.")
        @Test
        void changeEmptyExceptionIfInTableGroup() {
            final TableGroup tableGroup = save(new TableGroup());
            final OrderTable orderTable = save(createOrderTable(tableGroup));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조리, 식사 주문 상태를 가진 주문이 있는 주문 테이블은 변경할 수 없다.")
        @Test
        void changeEmptyExceptionIfHasStatus() {
            final MenuGroup menuGroup = save(createMenuGroup1());
            final Product product = save(createProduct1());
            final Menu menu = save(createMenu1(menuGroup, Collections.singletonList(product)));
            final OrderTable orderTable = save(createOrderTable());
            save(createOrder(orderTable, OrderStatus.COOKING, Collections.singletonList(createOrderLineItem(menu))));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 변경할 수 있다.")
        @Test
        void changeEmpty() {
            final OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), request);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.isEmpty()).isEqualTo(request.isEmpty())
            );
        }
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경은")
    @Nested
    class ChangeNumber extends SpringBootTestSupport {

        private OrderTableGuestRequest request;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            request = new OrderTableGuestRequest(15);
            orderTable = save(createOrderTable());
        }

        @DisplayName("존재하지 않는 주문 테이블일 경우 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsExceptionIfNotExist() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블일 경우 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsExceptionIfEmpty() {
            orderTable = save(createOrderTable(true));

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 변경할 수 있다.")
        @Test
        void changeNumberOfGuests() {
            final OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests())
            );
        }
    }
}
