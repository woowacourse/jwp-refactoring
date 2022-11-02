package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.order.dto.response.OrderTableResponse;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.order.dto.request.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.order.dto.request.CreateOrderTableRequest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블을 생성한다.")
        void create() {
            // given
            CreateOrderTableRequest request = new CreateOrderTableRequest(10, true);

            // when
            OrderTableResponse savedOrderTable = tableService.create(request);

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
            List<OrderTableResponse> tables = tableService.list();
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
            OrderTableResponse changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertThat(changedOrderTable.getEmpty()).isFalse();
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
            Order order = createOrder(savedOrderTable.getId());
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
            OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

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

    private Order createOrder(Long orderTableId) {
        Product product = productRepository.save(new Product("product", new BigDecimal(5000)));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));

        Menu menu = menuRepository.save(new Menu(
            "menu",
            new BigDecimal(2000),
            menuGroup.getId(),
            new ArrayList<MenuProductDto>() {{
                add(new MenuProductDto(product.getId(), 1L));
            }}
        ));

        return new Order(
            orderTableId,
            new ArrayList<OrderLineItemDto>() {{
                add(new OrderLineItemDto(menu.getId(), 1L));
            }}
        );
    }

}
