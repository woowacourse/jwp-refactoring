package kitchenpos.ui;

import static org.hamcrest.core.StringContains.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
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
    private final String BASE_URL = "/api/orders";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    private Order order;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
        order = new Order();
        order.setId(1L);
    }

    @Test
    void create() throws Exception {
        String body = objectMapper.writeValueAsString(order);

        given(orderService.create(any())).willReturn(order);

        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("1")));
    }

    @Test
    void list() throws Exception {
        Order order = new Order();
        order.setId(2L);

        given(orderService.list()).willReturn(Arrays.asList(this.order, order));

        mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("1")))
            .andExpect(content().string(containsString("2")));
    }

    @Test
    void changeOrderStatus() throws Exception {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        String body = objectMapper.writeValueAsString(order);

        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(order);

        mockMvc.perform(put(BASE_URL + "/1/order-status")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("COMPLETION")));
    }
}
