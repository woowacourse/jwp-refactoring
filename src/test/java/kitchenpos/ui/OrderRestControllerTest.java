package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    private static final long ORDER_ID = 1_000L;
    private static final long ORDER_LINE_ITEM_SEQ = 10L;
    private static final long ORDER_LINE_ITEM_MENU_ID = 100L;
    private static final String ORDER_LINE_ITEM_MENU_NAME = "후라이드+후라이드";
    private static final BigDecimal ORDER_LINE_ITEM_MENU_PRICE = BigDecimal.valueOf(19_000);
    private static final long ORDER_LINE_ITEM_MENU_GROUP_ID = 10L;
    private static final MenuGroup ORDER_LINE_ITEM_MENU_GROUP
        = MenuGroup.of(ORDER_LINE_ITEM_MENU_GROUP_ID, "메뉴 그룹");
    private static final long MENU_PRODUCT_SEQ = 100L;
    private static final long MENU_PRODUCT_PRODUCT_ID = 1_000L;
    private static final int MENU_PRODUCT_QUANTITY = 2;
    private static final long ORDER_LINE_QUANTITY = 10_000L;
    private static final long ORDER_TABLE_ID = 1L;
    private static final String ORDER_STATUS = OrderStatus.COOKING.name();

    private static final OrderLineItem orderLineItem;
    private static final Order order;

    static {
        MenuProduct MENU_PRODUCT = MenuProduct.of(MENU_PRODUCT_SEQ, null,
            Product.of(MENU_PRODUCT_PRODUCT_ID, "PRODUCTNAME", BigDecimal.ONE),
            MENU_PRODUCT_QUANTITY);
        Menu menu = Menu.of(ORDER_LINE_ITEM_MENU_ID, ORDER_LINE_ITEM_MENU_NAME,
            ORDER_LINE_ITEM_MENU_PRICE, ORDER_LINE_ITEM_MENU_GROUP,
            Collections.singletonList(MENU_PRODUCT));

        orderLineItem = OrderLineItem.of(ORDER_LINE_ITEM_SEQ, menu, ORDER_LINE_QUANTITY);

        Table table = Table.of(ORDER_TABLE_ID, 11, false);
        order = Order.of(ORDER_ID, table, ORDER_STATUS, LocalDateTime.now(),
            Collections.singletonList(orderLineItem));
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문 추가")
    @Test
    void create() throws Exception {
        String requestBody = "{\n"
            + "  \"orderTableId\": " + order.getTable().getId() + ",\n"
            + "  \"orderLineItems\": [\n"
            + "    {\n"
            + "      \"menuId\": " + orderLineItem.getMenu().getId() + ",\n"
            + "      \"quantity\": " + orderLineItem.getQuantity() + "\n"
            + "    }\n"
            + "  ]\n"
            + "}";

        given(orderService.create(any(OrderCreateRequest.class)))
            .willReturn(OrderResponse.of(order));

        ResultActions resultActions = mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(order.getId().intValue())))
            .andExpect(jsonPath("$.orderTableId", is(order.getTable().getId().intValue())))
            .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus())))
            .andExpect(jsonPath("$.orderLineItems", hasSize(1)))
            .andExpect(jsonPath("$.orderLineItems[0].seq", is(orderLineItem.getSeq().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].orderId",
                is(orderLineItem.getOrder().getId().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].menuId",
                is(orderLineItem.getMenu().getId().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].quantity",
                is((int) orderLineItem.getQuantity())))
            .andDo(print());
    }

    @DisplayName("주문 조회")
    @Test
    void list() throws Exception {
        given(orderService.list())
            .willReturn(OrderResponse.listOf(Collections.singletonList(order)));

        ResultActions resultActions = mockMvc.perform(get("/api/orders")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(order.getId().intValue())))
            .andDo(print());
    }

    @DisplayName("주문 상태 변경")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    void changeOrderStatus(OrderStatus orderStatus) throws Exception {
        order.changeOrderStatus(orderStatus);

        String requestBody = "{\n"
            + "  \"orderStatus\": \"" + order.getOrderStatus() + "\"\n"
            + "}";

        given(orderService.changeOrderStatus(anyLong(), any(OrderChangeStatusRequest.class)))
            .willReturn(OrderResponse.of(order));

        ResultActions resultActions = mockMvc
            .perform(put("/api/orders/" + ORDER_ID + "/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(order.getId().intValue())))
            .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus())))
            .andDo(print());
    }
}
