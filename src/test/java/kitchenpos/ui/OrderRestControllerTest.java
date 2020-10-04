package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
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
    private static final long ORDER_LINE_QUANTITY = 10_000L;
    private static final long ORDER_TABLE_ID = 1L;
    private static final String ORDER_STATUS = OrderStatus.COOKING.name();
    private static final LocalDateTime ORDERED_TIME = LocalDateTime.now();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문 추가")
    @Test
    void create() throws Exception {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(ORDER_LINE_ITEM_SEQ);
        orderLineItem.setOrderId(ORDER_ID);
        orderLineItem.setMenuId(ORDER_LINE_ITEM_MENU_ID);
        orderLineItem.setQuantity(ORDER_LINE_QUANTITY);

        Order order = new Order();
        order.setId(ORDER_ID);
        order.setOrderTableId(ORDER_TABLE_ID);
        order.setOrderStatus(ORDER_STATUS);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        String body = "{\n"
            + "  \"orderTableId\": " + order.getOrderTableId() + ",\n"
            + "  \"orderLineItems\": [\n"
            + "    {\n"
            + "      \"menuId\": " + orderLineItem.getMenuId() + ",\n"
            + "      \"quantity\": " + orderLineItem.getQuantity() + "\n"
            + "    }\n"
            + "  ]\n"
            + "}";

        given(orderService.create(any()))
            .willReturn(order);

        ResultActions resultActions = mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(order.getId().intValue())))
            .andExpect(jsonPath("$.orderTableId", is(order.getOrderTableId().intValue())))
            .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus())))
            .andExpect(jsonPath("$.orderLineItems", hasSize(1)))
            .andExpect(jsonPath("$.orderLineItems[0].seq", is(orderLineItem.getSeq().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].orderId",
                is(orderLineItem.getOrderId().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].menuId",
                is(orderLineItem.getMenuId().intValue())))
            .andExpect(jsonPath("$.orderLineItems[0].quantity",
                is((int) orderLineItem.getQuantity())))
            .andDo(print());
    }

    @DisplayName("주문 조회")
    @Test
    void list() throws Exception {
        Order order = new Order();
        order.setId(ORDER_ID);

        given(orderService.list())
            .willReturn(Collections.singletonList(order));

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
    @EnumSource(
        value = OrderStatus.class,
        names = {"MEAL", "COMPLETION"}
    )
    void changeOrderStatus(OrderStatus orderStatus) throws Exception {
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setOrderTableId(ORDER_TABLE_ID);
        order.setOrderStatus(orderStatus.name());

        String body = "{\n"
            + "  \"orderStatus\": \"" + order.getOrderStatus() + "\"\n"
            + "}";

        given(orderService.changeOrderStatus(any(), any()))
            .willReturn(order);

        ResultActions resultActions = mockMvc
            .perform(put("/api/orders/" + ORDER_ID + "/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(order.getId().intValue())))
            .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus())))
            .andDo(print());
    }
}
