package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@MockBean
	private OrderService orderService;

	private OrderLineItem orderLineItem;

	private Order order;

	private List<Order> orders;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setQuantity(1L);
		orderLineItem.setSeq(1L);

		order = new Order();
		order.setId(1L);
		order.setOrderTableId(1L);
		order.setOrderStatus("주문중");
		order.setOrderedTime(LocalDateTime.now());
		order.setOrderLineItems(Collections.singletonList(orderLineItem));

		orders = Collections.singletonList(order);
	}

	@Test
	public void create() throws Exception {
		given(orderService.create(any())).willReturn(order);

		mockMvc.perform(post("/api/orders")
			.content(objectMapper.writeValueAsString(order))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/orders/1"));
	}

	@Test
	void list() throws Exception {
		given(orderService.list()).willReturn(orders);

		mockMvc.perform(get("/api/orders")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void changeOrderStatus() throws Exception {
		given(orderService.changeOrderStatus(anyLong(), any())).willReturn(order);

		mockMvc.perform(put("/api/orders/1/order-status")
			.content(objectMapper.writeValueAsString(order))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}