package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@WebMvcTest(controllers = ProductRestController.class)
public class ProductRestControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private ProductService productService;
	private MockMvc mockMvc;
	private Product product;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
		product = new Product();
		product.setId(1L);
		product.setName("Fried");
		product.setPrice(BigDecimal.valueOf(16000));
	}

	@DisplayName("Product를 생성한다.")
	@Test
	void createTest() throws Exception {
		given(productService.create(any())).willReturn(product);

		mockMvc.perform(post("/api/products")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(product))
		)
			.andExpect(status().isCreated());
	}

	@DisplayName("등록한 모든 Product를 조회한다.")
	@Test
	void listTest() throws Exception {
		given(productService.list()).willReturn(Collections.singletonList(product));

		mockMvc.perform(get("/api/products")
			.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk());
	}
}
