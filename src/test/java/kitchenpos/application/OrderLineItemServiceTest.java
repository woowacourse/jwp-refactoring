package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderLineItemRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderLineItemServiceTest {
    private static final Integer 테이블_사람_1명 = 1;
    private static final Long 테이블_ID_1 = 1L;
    private static final Boolean 테이블_비어있지않음 = false;
    private static final Long 주문_메뉴_ID_1 = 1L;
    private static final Long 주문_메뉴_개수_1개 = 1L;
    private static final Long 주문_메뉴_개수_2개 = 2L;
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final Long 메뉴_ID_1 = 1L;
    private static final Long 메뉴_ID_2 = 2L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final String 메뉴_이름_코카콜라 = "코카 콜라";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final BigDecimal 메뉴_가격_1000원 = new BigDecimal("1000.0");
    private static final Long 주문_ID_1 = 1L;
    private static final LocalDateTime 주문_시간 = LocalDateTime.now();

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private MenuProductService menuProductService;


    private OrderLineItemService orderLineItemService;
    private OrderTable orderTable;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        orderLineItemService = new OrderLineItemService(orderLineItemRepository, menuRepository, menuProductService);

        orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        menuGroup = new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
    }

    @DisplayName("OrderLineItem 여러개의 생성이 올바르게 수행된다.")
    @Test
    void createOrderLineItemsTest() {
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        List<Menu> menus = Arrays.asList(
                new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup)
        );
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(주문_메뉴_ID_1, 주문_메뉴_개수_1개)
        );
        when(menuRepository.findAllById(anyList())).thenReturn(menus);

        orderLineItemService.createOrderLineItems(order, new OrderLineItemRequests(orderLineItemRequests));

        verify(orderLineItemRepository).save(any());
    }

    @DisplayName("OrderLineItem 생성 도중 조회한 메뉴 목록이 원하는 크기가 아니면, 예외가 발생한다.")
    @Test
    void createOrderLineItemsWithInvalidLengthExceptionTest() {
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        List<Menu> menus = Arrays.asList(
                new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup),
                new Menu(메뉴_ID_2, 메뉴_이름_코카콜라, 메뉴_가격_1000원, menuGroup)
        );
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(주문_메뉴_ID_1, 주문_메뉴_개수_1개)
        );
        when(menuRepository.findAllById(anyList())).thenReturn(menus);

        assertThatThrownBy(() -> orderLineItemService.createOrderLineItems(order, new OrderLineItemRequests(orderLineItemRequests)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품의 조회가 잘못되었습니다.");
    }

    @DisplayName("OrderLineItem 생성 도중 조회한 메뉴 목록이 비어있으면, 예외가 발생한다.")
    @Test
    void createOrderLineItemsWithEmptyExceptionTest() {
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(주문_메뉴_ID_1, 주문_메뉴_개수_1개)
        );
        when(menuRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> orderLineItemService.createOrderLineItems(order, new OrderLineItemRequests(orderLineItemRequests)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품의 조회가 잘못되었습니다.");
    }

    @DisplayName("Order로 해당하는 Menu를 조회할 경우 올바르게 수행된다.")
    @Test
    void findMenusByOrderTest() {
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        Menu menu1 = new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);
        Menu menu2 = new Menu(메뉴_ID_2, 메뉴_이름_코카콜라, 메뉴_가격_1000원, menuGroup);
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(order, menu1, 주문_메뉴_개수_1개),
                new OrderLineItem(order, menu2, 주문_메뉴_개수_2개)
        );
        when(orderLineItemRepository.findAllByOrder(any(Order.class))).thenReturn(orderLineItems);

        List<MenuResponse> foundMenus = orderLineItemService.findMenusByOrder(order);

        assertThat(foundMenus).
                hasSize(2).
                extracting("name").
                containsOnly(메뉴_이름_후라이드_치킨, 메뉴_이름_코카콜라);
    }
}
