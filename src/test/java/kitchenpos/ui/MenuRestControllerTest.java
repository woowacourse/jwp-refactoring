package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Money;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuCreateResponse;
import kitchenpos.dto.menu.MenuFindAllResponse;
import kitchenpos.dto.menu.MenuFindAllResponses;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@MockBean
	private MenuService menuService;

	private Menu menu;

	private List<Menu> menus;

	private MenuCreateRequest menuCreateRequest;

	private MenuCreateResponse menuCreateResponse;

	private MenuFindAllResponse menuFindAllResponse;

	private MenuFindAllResponses menuFindAllResponses;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		MenuProducts menuProducts = new MenuProducts(Collections.singletonList(new MenuProduct(1L, 1L, 1L, 1L)));

		menu = new Menu(1L, "메뉴", new Money(3000L), 1L, menuProducts);

		menus = Collections.singletonList(menu);

		menuCreateRequest = new MenuCreateRequest(menu);

		menuCreateResponse = new MenuCreateResponse(menu);

		menuFindAllResponse = new MenuFindAllResponse(menu);

		menuFindAllResponses = new MenuFindAllResponses(Collections.singletonList(menuFindAllResponse));
	}

	@Test
	void create() throws Exception {
		given(menuService.create(any())).willReturn(menuCreateResponse);

		mockMvc.perform(post("/api/menus")
			.content(objectMapper.writeValueAsString(menuCreateRequest))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/menus/1"));
	}

	@Test
	void list() throws Exception {
		given(menuService.list()).willReturn(menuFindAllResponses);

		mockMvc.perform(get("/api/menus")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}