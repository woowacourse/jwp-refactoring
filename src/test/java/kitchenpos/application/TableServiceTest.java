package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.table.ChangeOrderTableEmptyRequest;
import kitchenpos.dto.table.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.dto.table.CreateOrderTableRequest;

class TableServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블을 생성한다.")
        void create() {
            // given
            CreateOrderTableRequest request = new CreateOrderTableRequest(10, true);

            // when
            OrderTable savedOrderTable = tableService.create(request);

            // then
            assertThat(savedOrderTable.getId()).isNotNull();
        }

    }

    @Nested
    @DisplayName("list()")
    class ListMethod {

        @Test
        @DisplayName("전체 테이블을 조회한다.")
        void list() {
            List<OrderTable> tables = tableService.list();
            assertThat(tables).isNotNull();
        }

    }

    @Nested
    @DisplayName("changeEmpty()")
    class ChangeEmptyMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 상태를 변경한다.")
        void changeEmpty() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);
            ChangeOrderTableEmptyRequest request = new ChangeOrderTableEmptyRequest(false);

            // when
            OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertThat(changedOrderTable.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 테이블 id인 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            // given
            ChangeOrderTableEmptyRequest request = new ChangeOrderTableEmptyRequest(false);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(0L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 테이블입니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("COOKING 혹은 MEAL 상태인 경우 예외가 발생한다.")
        void invalidStatus(String status) {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);
            Order order = createOrder(savedOrderTable);
            order.changeStatus(status);
            orderRepository.save(order);

            ChangeOrderTableEmptyRequest request = new ChangeOrderTableEmptyRequest(false);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("변경할 수 있는 상태가 아닙니다.");
        }

    }

    @Nested
    @DisplayName("changeNumberOfGuests()")
    class ChangeNumberOfGuestsMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 방문자 수를 변경한다.")
        void changeNumberOfGuests() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(false);
            ChangeOrderTableNumberOfGuestRequest request = new ChangeOrderTableNumberOfGuestRequest(20);

            // when
            OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

            // then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(20);
        }

        @Test
        @DisplayName("존재하지 않은 테이블 id의 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            // given
            ChangeOrderTableNumberOfGuestRequest request = new ChangeOrderTableNumberOfGuestRequest(20);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 테이블입니다.");
        }

    }

    private OrderTable createAndSaveOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable(10, empty);
        return orderTableRepository.save(orderTable);
    }

    private Order createOrder(OrderTable orderTable) {
        Product product = productRepository.save(new Product("product", new BigDecimal(5000)));

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));

        Menu menu = menuRepository.save(new Menu(
            "menu",
            new BigDecimal(2000),
            menuGroup,
            new HashMap<Product, Long>() {{
                put(product, 1L);
            }}
        ));

        return new Order(
            orderTable,
            new HashMap<Menu, Long>() {{
                put(menu, 1L);
            }}
        );
    }

}
