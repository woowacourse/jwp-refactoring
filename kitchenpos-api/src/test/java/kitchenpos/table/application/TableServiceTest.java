package kitchenpos.table.application;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.cleaner.ApplicationTest;
import kitchenpos.table.application.dto.request.OrderTableCreateCommand;
import kitchenpos.table.application.dto.request.OrderTableEmptyCommand;
import kitchenpos.table.application.dto.request.OrderTableGuestCommand;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("OrderTable을 생성한다.")
    void create() {
        OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateCommand(10, true));

        assertAll(
                () -> assertThat(orderTableResponse.tableGroupId()).isNull(),
                () -> assertThat(orderTableResponse.numberOfGuests()).isEqualTo(10),
                () -> assertThat(orderTableResponse.empty()).isTrue()
        );
    }

    @Test
    @DisplayName("모든 OrderTable을 조회한다.")
    void list() {
        OrderTableResponse orderTableResponse1 = tableService.create(new OrderTableCreateCommand(10, true));
        OrderTableResponse orderTableResponse2 = tableService.create(new OrderTableCreateCommand(5, true));

        List<OrderTableResponse> orderTableResponses = tableService.list();
        assertThat(orderTableResponses).containsExactly(orderTableResponse1, orderTableResponse2);
    }

    @Nested
    @DisplayName("비어있는 상태로 만들 때 ")
    class ChangeEmptyTest {

        @Test
        @DisplayName("OrderTable이 존재하지 않으면 예외가 발생한다.")
        void orderTableNotFoundFailed() {
            assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTableEmptyCommand(true)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("OrderTable이 테이블 그룹에 속해있지 않을 경우 예외가 발생한다.")
        void orderTableNotInTableGroupFailed() {
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(10, true));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(10, true));
            tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), new OrderTableEmptyCommand(true)))
                    .isInstanceOf(InvalidOrderTableException.class)
                    .hasMessage("이미 테이블 그룹에 속해있습니다.");
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("OrderTable의 주문 상태가 COMPLETION이 아닐 경우 예외가 발생한다.")
        void orderTableOrdersNotCompletionFailed(final OrderStatus orderStatus) {
            Menu menu = createMenu(MENU1_NAME, MENU1_PRICE);
            OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateCommand(10, false));

            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(menu.getId(), 1),
                    new OrderLineItem(menu.getId(), 2));
            orderRepository.save(new Order(orderTableResponse.id(), orderStatus, LocalDateTime.now(), orderLineItems));

            assertThatThrownBy(
                    () -> tableService.changeEmpty(orderTableResponse.id(), new OrderTableEmptyCommand(true)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("현재 조리 / 식사 중입니다.");
        }

        @Test
        @DisplayName("정상적인 경우 성공한다.")
        void changeEmpty() {
            OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateCommand(10, true));
            OrderTableResponse updatedResponse = tableService.changeEmpty(orderTableResponse.id(),
                    new OrderTableEmptyCommand(false));

            assertThat(updatedResponse.empty()).isFalse();
        }
    }

    @Nested
    @DisplayName("테이블 인원 수를 바꿀 때 ")
    class ChangeNumberOfGuestsTest {

        @Test
        @DisplayName("음수로 바꿀 경우 예외가 발생한다.")
        void negativeNumberFailed() {
            OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateCommand(10, false));

            OrderTableGuestCommand orderTableGuestCommand = new OrderTableGuestCommand(-1);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.id(), orderTableGuestCommand))
                    .isInstanceOf(InvalidOrderTableException.class)
                    .hasMessage("테이블 인원은 음수일 수 없습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 경우 예외가 발생한다.")
        void orderTableNotFoundFailed() {
            OrderTableGuestCommand orderTableGuestCommand = new OrderTableGuestCommand(2);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTableGuestCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("테이블이 비어있으면 예외가 발생한다.")
        void orderTableEmptyFailed() {
            OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateCommand(10, true));

            OrderTableGuestCommand orderTableGuestCommand = new OrderTableGuestCommand(10);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.id(), orderTableGuestCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 비어있을 수 없습니다.");
        }

        @Test
        @DisplayName("정상적인 경우 인원 변경에 성공한다.")
        void changeNumberOfGuests() {
            OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateCommand(10, false));

            OrderTableGuestCommand orderTableGuestCommand = new OrderTableGuestCommand(20);
            OrderTableResponse result = tableService.changeNumberOfGuests(orderTableResponse.id(),
                    orderTableGuestCommand);
            assertThat(result.numberOfGuests()).isEqualTo(20);
        }
    }

    private Menu createMenu(final String name, final BigDecimal price) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product1 = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productRepository.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        List<MenuProduct> menuProducts = List.of(new MenuProduct(null, product1.getId(), 1),
                new MenuProduct(null, product2.getId(), 1));
        return menuRepository.save(new Menu(name, price, menuGroup.getId(), menuProducts));
    }
}
