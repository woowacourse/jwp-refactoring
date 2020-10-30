package kitchenpos.ui;

import static kitchenpos.OrderFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @DisplayName("정상적인 주문 생성 요청에 created 상태로 응답하는지 확인한다.")
    @Test
    void createTest() throws Exception {
        final Order savedOrder = createOrderWithId(1L);
        given(orderService.create(any(Order.class))).willReturn(savedOrder);

        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(createOrderWithoutId()))
        )
            .andExpect(status().isCreated())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(savedOrder)))
            .andExpect(header().exists("Location"));
    }

    @DisplayName("정상적인 주문 리스트 요청에 ok상태로 응답하는지 확인한다.")
    @Test
    void listTest() throws Exception {
        final List<Order> orders = createOrders();
        given(orderService.list()).willReturn(orders);

        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(orders)));
    }

    @DisplayName("정상적인 주문 상태 변경 요청에 ok 상태로 응답하는지 확인한다.")
    @Test
    void changeOrderStatusTest() throws Exception {
        final Order savedOrder = createOrderWithId(1L);
        savedOrder.setOrderStatus(OrderStatus.MEAL.name());
        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(savedOrder);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(createOrderWithoutId()))
        )
            .andExpect(status().isOk())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(savedOrder)));
    }
}

