package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@SpringBootTest
class OrderTableValidatorTest {

    @Autowired
    private OrderTableValidator orderTableValidator;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuValidator menuValidator;

    @Nested
    @DisplayName("validateGroup()")
    class ValidateGroupMethod {

        @Test
        @DisplayName("예외사항이 없다면 예외가 발생하지 않는다.")
        void validateGroup() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);

            // when, then
            assertDoesNotThrow(() -> orderTableValidator.validateGroup(savedOrderTable));
        }

        @Test
        @DisplayName("이미 그룹이 있는 경우 예외가 발생한다.")
        void alreadyGroup() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);
            savedOrderTable.group(1L, orderTableValidator);

            // when, then
            assertThatThrownBy(() -> orderTableValidator.validateGroup(savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 다른 그룹에 존재하는 테이블입니다.");
        }

        @Test
        @DisplayName("빈 테이블이 아닌 경우 예외가 발생한다.")
        void notEmpty() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(false);

            // when, then
            assertThatThrownBy(() -> orderTableValidator.validateGroup(savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블입니다.");
        }

    }

    @Nested
    @DisplayName("validateChangeEmpty()")
    class ValidateChangeEmptyMethod {

        @Test
        @DisplayName("예외사항이 없다면 예외가 발생하지 않는다.")
        void validateChangeEmpty() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);

            // when, then
            assertDoesNotThrow(() -> orderTableValidator.validateChangeEmpty(savedOrderTable));
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("COOKING 혹은 MEAL 상태인 경우 예외가 발생한다.")
        void invalidStatus(String status) {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);
            Order order = createOrder(savedOrderTable.getId());
            order.changeStatus(status, orderValidator);
            orderRepository.save(order);

            // when, then
            assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 시작되어 상태를 변경할 수 없습니다");
        }

        @Test
        @DisplayName("그룹이 있는 경우 예외가 발생한다.")
        void alreadyGroup() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);
            savedOrderTable.group(1L, orderTableValidator);

            // when, then
            assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블 그룹에 묶여있어 상태를 변경할 수 없습니다.");
        }

    }

    @Nested
    @DisplayName("validateChangeNumberOfGuests()")
    class ValidateChangeNumberOfGuestsMethod {

        @Test
        @DisplayName("예외사항이 없다면 예외가 발생하지 않는다.")
        void validateChangeNumberOfGuests() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(false);

            // when, then
            assertDoesNotThrow(() -> savedOrderTable.changeNumberOfGuests(10, orderTableValidator));
        }

        @Test
        @DisplayName("0 미만의 수인 경우 예외가 발생한다.")
        void lessThanZero() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(false);

            // when, then
            assertThatThrownBy(() -> savedOrderTable.changeNumberOfGuests(-1, orderTableValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수는 0 이상이어야 합니다.");
        }

        @Test
        @DisplayName("비어있는 테이블인 경우 예외가 발생한다.")
        void emptyTable() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(true);

            // when, then
            assertThatThrownBy(() -> savedOrderTable.changeNumberOfGuests(20, orderTableValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블입니다.");
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
            }},
            menuValidator
        ));

        return new Order(
            orderTableId,
            new ArrayList<OrderLineItemDto>() {{
                add(new OrderLineItemDto(menu.getName(), menu.getPrice(), 1L));
            }},
            orderValidator
        );
    }

}
