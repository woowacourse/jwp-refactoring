package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("주문 api")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private Order savedOrder;

    @BeforeEach
    void setUp() {
        savedOrder = Order.builder()
                .id(1L)
                .build();
    }

    @Test
    void create() throws Exception {
        final String content = objectMapper.writeValueAsString(new Order());
        when(orderService.create(any())).thenReturn(savedOrder);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/orders")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.getHeader("Location")).isEqualTo("/api/orders/1")
        );
    }

    @Test
    void list() throws Exception {
        when(orderService.list()).thenReturn(Collections.singletonList(new Order()));

        final MockHttpServletResponse response = mockMvc.perform(get("/api/orders"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void changeOrderStatus() throws Exception {
        final String content = objectMapper.writeValueAsString(new Order());
        when(orderService.changeOrderStatus(any(), any())).thenReturn(savedOrder);

        final MockHttpServletResponse response = mockMvc.perform(put("/api/orders/1/order-status")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
