package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends MvcTest {

    private static final Long SEQ_1 = 1L;
    private static final Long MENU_ID_1 = 1L;
    private static final long QUANTITY_1 = 1L;
    private static final OrderLineItem ORDER_LINE_ITEM_1 = new OrderLineItem();

    private static final Long SEQ_2 = 2L;
    private static final Long MENU_ID_2 = 2L;
    private static final long QUANTITY_2 = 2L;
    private static final OrderLineItem ORDER_LINE_ITEM_2 = new OrderLineItem();

    private static final Long ORDER_ID_1 = 1L;
    private static final Long ORDER_TABLE_ID_1 = 1L;
    private static final String ORDER_STATUS_1 = "COOKING";
    private static final LocalDateTime ORDERED_TIME_1 = LocalDateTime.parse("2018-12-15T10:00:00");
    private static final List<OrderLineItem> ORDER_LIKE_ITEMS_1 = Arrays.asList(ORDER_LINE_ITEM_1);
    private static final Order ORDER_1 = new Order();

    private static final Long ORDER_ID_2 = 2L;
    private static final Long ORDER_TABLE_ID_2 = 2L;
    private static final String ORDER_STATUS_2 = "COOKING";
    private static final LocalDateTime ORDERED_TIME_2 = LocalDateTime.parse("2018-12-16T10:00:00");
    private static final List<OrderLineItem> ORDER_LIKE_ITEMS_2 = Arrays.asList(ORDER_LINE_ITEM_2);
    private static final Order ORDER_2 = new Order();

    @MockBean
    private OrderService orderService;

    private
    @BeforeEach
    void setUp() {
        ORDER_LINE_ITEM_1.setSeq(SEQ_1);
        ORDER_LINE_ITEM_1.setOrderId(ORDER_ID_1);
        ORDER_LINE_ITEM_1.setMenuId(MENU_ID_1);
        ORDER_LINE_ITEM_1.setQuantity(QUANTITY_1);

        ORDER_LINE_ITEM_2.setSeq(SEQ_2);
        ORDER_LINE_ITEM_2.setOrderId(ORDER_ID_2);
        ORDER_LINE_ITEM_2.setMenuId(MENU_ID_2);
        ORDER_LINE_ITEM_2.setQuantity(QUANTITY_2);

        ORDER_1.setId(ORDER_ID_1);
        ORDER_1.setOrderTableId(ORDER_TABLE_ID_1);
        ORDER_1.setOrderStatus(ORDER_STATUS_1);
        ORDER_1.setOrderedTime(ORDERED_TIME_1);
        ORDER_1.setOrderLineItems(ORDER_LIKE_ITEMS_1);

        ORDER_2.setId(ORDER_ID_2);
        ORDER_2.setOrderTableId(ORDER_TABLE_ID_2);
        ORDER_2.setOrderStatus(ORDER_STATUS_2);
        ORDER_2.setOrderedTime(ORDERED_TIME_2);
        ORDER_2.setOrderLineItems(ORDER_LIKE_ITEMS_2);
    }

    @DisplayName("/api/orders로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(orderService.create(any())).willReturn(ORDER_1);

        String inputJson = objectMapper.writeValueAsString(ORDER_1);
        MvcResult mvcResult = postAction("/api/orders", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/orders/1"))
            .andReturn();

        Order orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(ORDER_1);
    }

    @DisplayName("/api/orders로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(ORDER_1, ORDER_2));

        MvcResult mvcResult = getAction("/api/orders")
            .andExpect(status().isOk())
            .andReturn();

        List<Order> ordersResponse = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Order>>() {});
        assertAll(
            () -> assertThat(ordersResponse.size()).isEqualTo(2),
            () -> assertThat(ordersResponse.get(0)).usingRecursiveComparison().isEqualTo(ORDER_1),
            () -> assertThat(ordersResponse.get(1)).usingRecursiveComparison().isEqualTo(ORDER_2)
        );
    }

    @DisplayName("/api/orders/{orderId}/order-status로 PUT요청 성공 테스트")
    @Test
    void changeOrderStatusTest() throws Exception {
        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(ORDER_1);

        String inputJson = objectMapper.writeValueAsString(ORDER_1);
        MvcResult mvcResult = putAction("/api/orders/1/order-status", inputJson)
            .andExpect(status().isOk())
            .andReturn();

        Order orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(ORDER_1);
    }
}