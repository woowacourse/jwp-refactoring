package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderAlreadyCompletionException;
import kitchenpos.order.exception.OrderLineItemDuplicateException;
import kitchenpos.order.exception.OrderLineItemsEmptyException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableEmptyException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Order Service 테스트")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Order를 생성할 때")
    @Nested
    class createOrder {

        @DisplayName("Order의 OrderLineItem이 없다면 예외가 발생한다.")
        @Test
        void orderLineItemsEmptyException() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다(2, tableGroup));
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menuResponse = menuService.create(MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts));

            OrderRequest request = OrderRequest를_생성한다(orderTable, new ArrayList<>());

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderLineItemsEmptyException.class);
        }

        @DisplayName("동일한 메뉴를 시킬 때 Quantity가 아닌 OrderLineItem을 추가해서 OrderLineItem 개수와 Menu 개수가 다를 경우 예외가 발생한다.")
        @Test
        void orderLineItemCountNonMatchWithMenuException() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다(2, tableGroup));
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu = menuService.create(MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts));

            OrderLineItemRequest itemRequest1 = OrderLineItemRequest를_생성한다(menu, 1);
            OrderLineItemRequest itemRequest2 = OrderLineItemRequest를_생성한다(menu, 1);

            OrderRequest request = OrderRequest를_생성한다(orderTable, Arrays.asList(itemRequest1, itemRequest2));

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderLineItemDuplicateException.class);
        }

        @DisplayName("orderTable이 실제로 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void orderTableNotFoundException() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = menuService.create(MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts));
            MenuResponse menu2 = menuService.create(MenuRequest를_생성한다("색다른 메뉴", 4_600, menuGroup, menuProducts));

            OrderLineItemRequest itemRequest1 = OrderLineItemRequest를_생성한다(menu1, 1);
            OrderLineItemRequest itemRequest2 = OrderLineItemRequest를_생성한다(menu2, 1);

            OrderTable 없는_테이블 = new OrderTable(-1L, TableGroup.create(), 5, true);
            OrderRequest request = OrderRequest를_생성한다(없는_테이블, Arrays.asList(itemRequest1, itemRequest2));

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
        }

        @DisplayName("Order의 OrderTable의 상태가 이미 Empty일 경우 예외가 발생한다.")
        @Test
        void orderTableStatusEmptyException() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(TableGroup을_생성한다());
            OrderTable orderTable = OrderTable을_생성한다(2, tableGroup, false);
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = menuService.create(MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts));
            MenuResponse menu2 = menuService.create(MenuRequest를_생성한다("색다른 메뉴", 4_600, menuGroup, menuProducts));

            OrderLineItemRequest itemRequest1 = OrderLineItemRequest를_생성한다(menu1, 1);
            OrderLineItemRequest itemRequest2 = OrderLineItemRequest를_생성한다(menu2, 1);

            orderTable.ungroup();
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            OrderRequest request = OrderRequest를_생성한다(savedOrderTable, Arrays.asList(itemRequest1, itemRequest2));

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderTableEmptyException.class);
        }

        @DisplayName("정상적으로 저장될 경우 orderLineItem도 함께 저장된다.")
        @Test
        void success() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다(2, tableGroup));
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = menuService.create(MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts));
            MenuResponse menu2 = menuService.create(MenuRequest를_생성한다("색다른 메뉴", 4_600, menuGroup, menuProducts));

            OrderLineItemRequest itemRequest1 = OrderLineItemRequest를_생성한다(menu1, 1);
            OrderLineItemRequest itemRequest2 = OrderLineItemRequest를_생성한다(menu2, 1);

            OrderRequest request = OrderRequest를_생성한다(orderTable, Arrays.asList(itemRequest1, itemRequest2));

            // when
            OrderResponse response = orderService.create(request);

            // then
            assertThat(response.getId()).isNotNull();
            assertThat(response.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now());
            assertThat(response.getOrderStatus()).isEqualTo(COOKING.name());
            assertThat(response.getOrderTable()).isEqualTo(request.getOrderTableId());
            assertThat(response.getOrderLineItems()).extracting("seq").isNotEmpty();
        }
    }

    @DisplayName("모든 Order를 조회한다.")
    @Test
    void list() {
        // given
        List<OrderResponse> beforeSavedOrders = orderService.list();
        beforeSavedOrders.add(orderService를_통해_Order를_생성한다());

        // when
        List<OrderResponse> afterSavedOrders = orderService.list();

        // then
        assertThat(afterSavedOrders).hasSize(beforeSavedOrders.size());
        assertThat(afterSavedOrders).usingRecursiveComparison()
            .isEqualTo(beforeSavedOrders);
    }

    @DisplayName("Order의 상태를 변경할 때")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("ID에 해당하는 Order가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderIdException() {
            // given
            OrderStatusRequest request = Status_변화용_Request를_생성한다(COOKING);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, request))
                .isExactlyInstanceOf(OrderNotFoundException.class);
        }

        @DisplayName("Order의 상태가 이미 COMPLETION일 경우 예외가 발생한다.")
        @Test
        void alreadyCompletionStatusException() {
            // given
            OrderResponse beforeOrderResponse = orderService를_통해_Order를_생성한다();
            OrderStatusRequest request = Status_변화용_Request를_생성한다(COMPLETION);

            orderService.changeOrderStatus(beforeOrderResponse.getId(), request);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(beforeOrderResponse.getId(), Status_변화용_Request를_생성한다(COOKING)))
                .isExactlyInstanceOf(OrderAlreadyCompletionException.class);
        }

        @DisplayName("Order의 상태가 정상적으로 변경되고 반환된다.")
        @Test
        void success() {
            // given
            OrderResponse beforeOrderResponse = orderService를_통해_Order를_생성한다();
            OrderStatusRequest request = Status_변화용_Request를_생성한다(COMPLETION);

            // when
            OrderResponse response = orderService.changeOrderStatus(beforeOrderResponse.getId(), request);

            // then
            assertThat(response.getOrderStatus()).isEqualTo(request.getOrderStatus());
        }
    }

    private OrderResponse orderService를_통해_Order를_생성한다() {
        TableGroup tableGroup = tableGroupRepository.save(TableGroup을_생성한다());
        OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다(2, tableGroup));
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));

        Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
        Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
        MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
        MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
        List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        MenuResponse menu1 = menuService.create(MenuRequest를_생성한다("엄청난 메뉴", 5_600, menuGroup, menuProducts));
        MenuResponse menu2 = menuService.create(MenuRequest를_생성한다("색다른 메뉴", 4_600, menuGroup, menuProducts));

        OrderLineItemRequest itemRequest1 = OrderLineItemRequest를_생성한다(menu1, 1);
        OrderLineItemRequest itemRequest2 = OrderLineItemRequest를_생성한다(menu2, 1);

        return orderService.create(OrderRequest를_생성한다(orderTable, Arrays.asList(itemRequest1, itemRequest2)));
    }

    private OrderRequest OrderRequest를_생성한다(OrderTable orderTable, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTable.getId(), orderLineItems);
    }

    private OrderStatusRequest Status_변화용_Request를_생성한다(OrderStatus orderStatus) {
        return new OrderStatusRequest(orderStatus.name());
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, TableGroup tableGroup) {
        return OrderTable을_생성한다(numberOfGuests, tableGroup, false);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, TableGroup tableGroup, boolean isEmpty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, isEmpty);
        orderTable.groupBy(tableGroup);

        return orderTable;
    }

    private TableGroup TableGroup을_생성한다() {
        return TableGroup.create();
    }

    private OrderLineItemRequest OrderLineItemRequest를_생성한다(MenuResponse menu, long quantity) {
        return new OrderLineItemRequest(menu.getId(), quantity);
    }

    private MenuRequest MenuRequest를_생성한다(String name, int price, MenuGroup menuGroup, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, BigDecimal.valueOf(price), menuGroup.getId(), menuProducts);
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);

        return menuGroup;
    }

    private MenuProductRequest MenuProductRequest를_생성한다(Product product, long quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }

    private Product Product를_생성한다(String name, int price) {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
