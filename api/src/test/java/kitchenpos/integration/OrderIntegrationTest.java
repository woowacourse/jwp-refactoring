package kitchenpos.integration;

import java.math.BigDecimal;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.domain.*;
import kitchenpos.dto.request.ChangeNamePriceRequest;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.CreateOrderResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order 통합 테스트")
@SpringBootTest
@Transactional
class OrderIntegrationTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("메뉴(Menu)의 이름, 가격을 변경해도 변경 전 주문 항목(OrderLineItem)의 메뉴 이름, 가격은 변경되지 않는다.")
    void notAffectBeforeChangeMenu() {
        // given
        MenuGroup 치킨_메뉴 = menuGroup_생성("치킨 메뉴");
        Menu originalMenu = menu_생성("Old 후라이드 set", BigDecimal.valueOf(13000), 치킨_메뉴.getId());

        OrderTable table = table_생성(4, false);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                table.getId(),
                Collections.singletonList(new OrderLineItemRequest(originalMenu.getId(), 1))
        );
        CreateOrderResponse order = orderService.create(createOrderRequest);

        ChangeNamePriceRequest changeRequest = new ChangeNamePriceRequest(
                "New 후라이드 set",
                BigDecimal.valueOf(15000)
        );

        // when
        menuService.changeNamePrice(originalMenu.getId(), changeRequest);
        Menu changedMenu = menuRepository.findById(originalMenu.getId()).get();

        Order currentOrder = orderRepository.findById(order.getId()).get();
        OrderLineItem orderLineItem = currentOrder.getOrderLineItemLists().get(0);
        Menu orderedMenu = menuRepository.findById(orderLineItem.getMenuId()).get();

        // then
        assertThat(changedMenu.getName()).isNotEqualTo(orderedMenu.getName());
        assertThat(changedMenu.getPrice()).isNotEqualTo(orderedMenu.getPrice());
    }

    private MenuGroup menuGroup_생성(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupRepository.save(menuGroup);
    }

    private Menu menu_생성(String name, BigDecimal price, Long menuGroupId) {
        Menu menu = new Menu(name, price, menuGroupId);
        return menuRepository.save(menu);
    }

    private OrderTable table_생성(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return orderTableRepository.save(orderTable);
    }
}
