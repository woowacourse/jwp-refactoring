package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.testutils.TestDomainBuilder.menuBuilder;
import static kitchenpos.testutils.TestDomainBuilder.menuProductBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("POST /api/menus - (이름, 가격, 메뉴 그룹 아이디, 복수의 메뉴 상품)으로 메뉴를 추가한다.")
    @Test
    void create() throws Exception {
        // given
        String name = "후라이드+후라이드";
        BigDecimal price = BigDecimal.valueOf(19000);
        Long menuGroupId = 1L;
        MenuProduct newMenuProduct = menuProductBuilder()
                .productId(1L)
                .quantity(2)
                .build();
        Menu newMenu = menuBuilder()
                .name(name)
                .price(price)
                .menuGroupId(menuGroupId)
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        MenuProduct expectedMenuProduct = menuProductBuilder()
                .seq(1L)
                .menuId(1L)
                .productId(1L)
                .quantity(2)
                .build();
        Menu expectedMenu = menuBuilder()
                .id(1L)
                .name(name)
                .price(price)
                .menuGroupId(menuGroupId)
                .menuProducts(Collections.singletonList(expectedMenuProduct))
                .build();

        given(menuService.create(any(Menu.class))).willReturn(expectedMenu);

        // when
        MockHttpServletResponse response =
                mockMvc.perform(postApiMenus(newMenu))
                        .andReturn()
                        .getResponse();

        String responseBody = response.getContentAsString(Charset.defaultCharset());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("location")).isEqualTo("/api/menus/" + expectedMenu.getId());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(expectedMenu));

        then(menuService).should(times(1)).create(any(Menu.class));
    }

    private RequestBuilder postApiMenus(Menu newMenu) throws JsonProcessingException {
        return post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMenu));
    }

    @DisplayName("GET /api/menus - 전체 메뉴의 리스트를 가져온다.")
    @Test
    void list() throws Exception {
        // given
        Menu friedChickenMenu = menuBuilder()
                .id(1L)
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(2L)
                .build();
        Menu seasoningChickenMenu = menuBuilder()
                .id(2L)
                .name("양념치킨")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(2L)
                .build();

        List<Menu> expectedMenus = Arrays.asList(friedChickenMenu, seasoningChickenMenu);

        given(menuService.list()).willReturn(expectedMenus);

        // when
        MockHttpServletResponse response =
                mockMvc.perform(getApiMenus())
                        .andReturn()
                        .getResponse();

        String responseBody = response.getContentAsString(Charset.defaultCharset());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(expectedMenus));

        then(menuService).should(times(1)).list();
    }

    private RequestBuilder getApiMenus() {
        return get("/api/menus");
    }
}
