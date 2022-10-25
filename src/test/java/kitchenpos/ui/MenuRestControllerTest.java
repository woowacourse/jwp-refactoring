package kitchenpos.ui;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_PRICE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class MenuRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("Menu를 생성한다.")
    void create() throws Exception {
        MenuRequest menuRequest = new MenuRequest(MENU1_NAME, MENU1_PRICE, 1L,
                List.of(
                        new MenuProductRequest(1L, 1),
                        new MenuProductRequest(2L, 2)
                ));

        Menu menu = new Menu(1L, MENU1_NAME, MENU1_PRICE, 1L);
        menu.addMenuProducts(List.of(new MenuProduct(menu.getId(), 1L, 1)));
        menu.addMenuProducts(List.of(new MenuProduct(menu.getId(), 2L, 2)));
        MenuResponse menuResponse = MenuResponse.from(menu);
        given(menuService.create(any(MenuCommand.class))).willReturn(menuResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/menus/1"))
                .andDo(print());
    }

    @Test
    void list() throws Exception {
        Menu menu1 = new Menu(1L, MENU1_NAME, MENU1_PRICE, 1L);
        menu1.addMenuProducts(List.of(new MenuProduct(menu1.getId(), 1L, 1)));

        Menu menu2 = new Menu(2L, MENU2_NAME, MENU2_PRICE, 1L);
        menu2.addMenuProducts(List.of(new MenuProduct(menu2.getId(), 2L, 1)));
        List<Menu> menus = List.of(menu1, menu2);
        given(menuService.list()).willReturn(menus);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/menus"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(menus)));
    }
}
