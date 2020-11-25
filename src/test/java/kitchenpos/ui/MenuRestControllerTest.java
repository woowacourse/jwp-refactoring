package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends MvcTest {

    @MockBean
    private MenuService menuService;

    @DisplayName("/api/menus로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(menuService.create(any())).willReturn(MENU_1);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                MENU_NAME_1,
                MENU_PRICE_1.getValue().longValue(),
                MENU_GROUP_ID_1,
                Arrays.asList(new MenuProductRequest(PRODUCT_ID_1, MENU_PRODUCT_QUANTITY_1))
            );
        String inputJson = objectMapper.writeValueAsString(menuCreateRequest);
        MvcResult mvcResult = postAction("/api/menus", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/menus/%d", MENU_ID_1)))
            .andReturn();

        Menu menuResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Menu.class);
        assertThat(menuResponse).usingRecursiveComparison().isEqualTo(MENU_1);
    }

    @DisplayName("/api/menus로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(MENU_1, MENU_2));

        MvcResult mvcResult = getAction("/api/menus")
            .andExpect(status().isOk())
            .andReturn();

        List<Menu> menusResponse = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Menu>>() {});
        assertAll(
            () -> assertThat(menusResponse.size()).isEqualTo(2),
            () -> assertThat(menusResponse.get(0)).usingRecursiveComparison().isEqualTo(MENU_1),
            () -> assertThat(menusResponse.get(1)).usingRecursiveComparison().isEqualTo(MENU_2)
        );
    }
}