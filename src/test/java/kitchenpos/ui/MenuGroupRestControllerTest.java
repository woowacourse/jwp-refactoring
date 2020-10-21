package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
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
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@MockBean
	private MenuGroupService menuGroupService;

	private MenuGroup menuGroup;

	private List<MenuGroup> menuGroups;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();

		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();

		menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("메뉴그룹");

		menuGroups = Collections.singletonList(menuGroup);
	}

	@Test
	void create() throws Exception {
		given(menuGroupService.create(any())).willReturn(menuGroup);

		mockMvc.perform(post("/api/menu-groups")
			.content(objectMapper.writeValueAsString(menuGroup))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/menu-groups/1"));
	}

	@Test
	void list() throws Exception {
		given(menuGroupService.list()).willReturn(menuGroups);

		mockMvc.perform(get("/api/menu-groups")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}