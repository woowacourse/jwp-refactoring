package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(controllers = OrderRestController.class)
public class OrderRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private OrderService orderService;
	private MockMvc mockMvc;
	private Order order;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();

		order = new Order();
		order.setId(1L);
		order.setOrderStatus(OrderStatus.COOKING.name());
	}

	@DisplayName("order를 생성한다.")
	@Test
	void createTest() throws Exception {
		given(orderService.create(any())).willReturn(order);

		mockMvc.perform(post("/api/orders")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(order))
		)
			.andExpect(status().isCreated());
	}

	@DisplayName("등록된 모든 Order를 조회한다.")
	@Test
	void listTest() throws Exception {
		given(orderService.list()).willReturn(Collections.singletonList(order));

		mockMvc.perform(get("/api/orders")
			.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk());
	}

	@DisplayName("Order의 상태를 변경한다.")
	@Test
	void changeOrderStatusTest() throws Exception {
		Order changed = new Order();
		order.setOrderStatus(OrderStatus.MEAL.name());
		given(orderService.changeOrderStatus(anyLong(), any())).willReturn(changed);

		mockMvc.perform(put("/api/orders/{id}/order-status", 1L)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(changed))
		)
			.andExpect(status().isOk());
	}
}
