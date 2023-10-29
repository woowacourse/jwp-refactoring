package kitchenpos.order.application;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.order.application.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.response.OrderHistoryResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Sql("/truncate.sql")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("새로운 주문 등록")
    @Nested
    class CreateNestedTest {

        @DisplayName("[SUCCESS] 새로운 주문을 등록한다.")
        @Test
        void success_create() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createOrderTable(5, true);
            final OrderSheet requestOrderSheet = new OrderSheet(
                    savedOrderTable.getId(),
                    List.of(
                            new OrderSheet.OrderSheetItem(savedMenu.getId(), 10L)
                    )
            );

            // when
            final OrderHistoryResponse actual = orderService.create(requestOrderSheet);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTableId()).isEqualTo(requestOrderSheet.getOrderTableId());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
                softly.assertThat(actual.getOrderedTime()).isBefore(LocalDateTime.now());
                softly.assertThat(actual.getOrderLineItems()).hasSize(requestOrderSheet.getOrderSheetItems().size());
            });
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록이 비어있는 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsIsEmpty() {
            // given
            final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(5, false));

            // when
            final OrderSheet requestOrderSheet = new OrderSheet(
                    savedOrderTable.getId(),
                    Collections.emptyList()
            );

            // then
            assertThatThrownBy(() -> orderService.create(requestOrderSheet))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 항목 목록 수량이 실제 메뉴의 수량과 다를 경우 예외가 발생한다.")
        @Test
        void throwException_create_order_when_orderLineItemsSize_IsNotEqualTo_menuCountsSize() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(5, false));

            // when
            final OrderSheet requestOrderSheet = new OrderSheet(
                    savedOrderTable.getId(),
                    List.of(
                            new OrderSheet.OrderSheetItem(savedMenu.getId(), 10L),
                            new OrderSheet.OrderSheetItem(-1L, 10L)
                    )
            );

            // then
            assertThatThrownBy(() -> orderService.create(requestOrderSheet))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Menu createMenu() {
        final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("1000")));
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu menu = Menu.withEmptyMenuProducts(
                new Name("테스트용 메뉴명"),
                Price.from("0"),
                savedMenuGroup
        );
        menu.addMenuProducts(List.of(
                MenuProduct.withoutMenu(savedProduct, new Quantity(10))
        ));

        return menuRepository.save(menu);
    }

    private OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        return orderTableRepository.save(OrderTable.withoutTableGroup(numberOfGuests, empty));
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("[SUCCESS] 주문 상태를 변경한다.")
        @Test
        void success_changeOrderStatus() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(5, true));
            final OrderSheet requestOrderSheet = new OrderSheet(
                    savedOrderTable.getId(),
                    List.of(
                            new OrderSheet.OrderSheetItem(savedMenu.getId(), 10L)
                    )
            );

            // when
            final OrderHistoryResponse expected = orderService.create(requestOrderSheet);


            // when
            final OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());
            final OrderHistoryResponse actual = orderService.changeOrderStatus(expected.getId(), request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getOrderTableId())
                        .usingRecursiveComparison()
                        .isEqualTo(expected.getOrderTableId());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
                softly.assertThat(actual.getOrderedTime()).isEqualTo(expected.getOrderedTime());
            });
        }

        @DisplayName("[EXCEPTION] 주문이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_when_order_isNotExists() {
            final OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());
            final Long wrongOrderId = -1L;
            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, request))
                    .isInstanceOf(EmptyResultDataAccessException.class);
        }

        @DisplayName("[EXCEPTION] 주문이 완료 상태에서 완료 상태로 변경할 경우 예외가 발생한다.")
        @Test
        void throwException_when_orderStatus_is_COMPLETION() {
            // given
            final Menu savedMenu = createMenu();
            final OrderTable savedOrderTable = createOrderTable(5, true);
            final OrderSheet requestOrderSheet = new OrderSheet(
                    savedOrderTable.getId(),
                    List.of(
                            new OrderSheet.OrderSheetItem(savedMenu.getId(), 10L)
                    )
            );

            // when
            final OrderHistoryResponse orderResponse = orderService.create(requestOrderSheet);

            // when
            final OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(orderResponse.getId(), request);

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
