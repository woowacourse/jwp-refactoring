package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
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

    private OrderLineItemService orderLineItemService;
    private OrderTable orderTable;
    private MenuGroup menuGroup;
    private Order order;
    private List<OrderLineItemRequest> orderLineItemRequests;

    @BeforeEach
    void setUp() {
        orderLineItemService = new OrderLineItemService(orderLineItemRepository, menuRepository);

        orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        menuGroup = new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(주문_메뉴_ID_1, 주문_메뉴_개수_1개)
        );
    }

    @DisplayName("OrderLineItem 여러 개의 생성이 올바르게 수행된다.")
    @Test
    void createOrderLineItemsTest() {
        List<Menu> menus = Arrays.asList(

        );
        Menu menu = new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);
        when(menuRepository.findById(anyLong())).thenReturn(java.util.Optional.of(menu));

        orderLineItemService.createOrderLineItems(order, orderLineItemRequests);

        verify(orderLineItemRepository).saveAll(anyList());
    }

    @DisplayName("OrderLineItem 생성 도중 null인 주문이 전달되면, 예외가 발생한다.")
    @Test
    void createOrderLineItemsWithNullOrderExceptionTest() {

        assertThatThrownBy(() -> orderLineItemService.createOrderLineItems(null, orderLineItemRequests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 주문이 입력되었습니다.");
    }

    @DisplayName("OrderLineItem 생성 도중 메뉴를 조회할 수 없으면, 예외가 발생한다.")
    @Test
    void createOrderLineItemsWithInvalidLengthExceptionTest() {
        when(menuRepository.findById(anyLong())).thenThrow(new IllegalArgumentException("주문 메뉴를 찾을 수 없습니다."));

        assertThatThrownBy(() -> orderLineItemService.createOrderLineItems(order, orderLineItemRequests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 메뉴를 찾을 수 없습니다.");
    }

    @DisplayName("OrderLineItem 생성 도중 조회한 메뉴 목록이 비어있으면, 예외가 발생한다.")
    @Test
    void createOrderLineItemsWithEmptyExceptionTest() {
        assertThatThrownBy(() -> orderLineItemService.createOrderLineItems(order, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 메뉴가 입력되었습니다.");
    }

    @DisplayName("OrderLineItem 생성 도중 조회한 메뉴 목록이 null이면, 예외가 발생한다.")
    @Test
    void createOrderLineItemsWithNullExceptionTest() {
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);

        assertThatThrownBy(() -> orderLineItemService.createOrderLineItems(order, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 메뉴가 입력되었습니다.");
    }
}
