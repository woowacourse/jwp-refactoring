package kitchenpos.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;

	private Product product;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		product = new Product();
		product.setId(1L);
		product.setPrice(BigDecimal.valueOf(1000));
		product.setName("김");
	}

	@Test
	void create() throws Exception {
		given(productService.create(any(Product.class))).willReturn(product);

		mockMvc.perform(post("/api/products")
			.content(objectMapper.writeValueAsString(product))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/products/1"));
	}

	@Test
	void list() throws Exception {
		given(productService.list()).willReturn(Collections.singletonList(product));

		mockMvc.perform(get("/api/products")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}