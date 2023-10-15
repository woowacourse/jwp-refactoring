//package kitchenpos.ui;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.time.LocalDateTime;
//import java.util.List;
//import kitchenpos.application.OrderService;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderStatus;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(OrderRestController.class)
//class OrderRestControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private OrderService orderService;
//
//    @Test
//    void create() throws Exception {
//        // given
//        final Order result = new Order();
//        result.setId(1L);
//        given(orderService.create(any())).willReturn(result);
//
//        final Order request = new Order();
//        request.setId(1L);
//        request.setOrderStatus(OrderStatus.COOKING.name());
//        request.setOrderedTime(LocalDateTime.now());
//
//        // when
//        mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", "/api/orders/1"));
//    }
//
//    @Test
//    void list() throws Exception {
//        // given
//        final Order resultA = new Order();
//        resultA.setOrderStatus(OrderStatus.COOKING.name());
//        resultA.setOrderedTime(LocalDateTime.now());
//        final Order resultB = new Order();
//        resultB.setOrderStatus(OrderStatus.COOKING.name());
//        resultB.setOrderedTime(LocalDateTime.now());
//        given(orderService.list()).willReturn(List.of(resultA, resultB));
//
//        // when
//        mockMvc.perform(get("/api/orders"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(List.of(resultA, resultB))));
//    }
//
//    @Test
//    void changeOrderStatus() throws Exception {
//        // given
//        final Order result = new Order();
//        result.setId(1L);
//        result.setOrderStatus(OrderStatus.COMPLETION.name());
//        given(orderService.changeOrderStatus(any(), any())).willReturn(result);
//
//        final Order request = new Order();
//        request.setOrderStatus(OrderStatus.COMPLETION.name());
//
//        // when
//        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(result)));
//    }
//}
