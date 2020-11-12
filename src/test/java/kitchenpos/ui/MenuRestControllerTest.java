package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuService menuService;

    @Test
    void create() throws Exception {
        Product product = new Product("product1", BigDecimal.ONE);
        Product persistProduct = productDao.save(product);

        MenuProductRequest menuProduct = new MenuProductRequest(persistProduct.getId(), 10);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        MenuRequest request = new MenuRequest("menu1", BigDecimal.TEN, persistMenuGroup.getId(),
            Arrays.asList(menuProduct));

        String response = mockMvc.perform(post("/api/menus")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        MenuResponse menuResponse = mapper.readValue(response, MenuResponse.class);

        assertAll(
            () -> assertThat(menuResponse.getPrice().longValue()).isEqualTo(request.getPrice().longValue()),
            () -> assertThat(menuResponse).isEqualToComparingOnlyGivenFields(request, "name", "menuGroupId"),
            () -> assertThat(menuResponse.getMenuProducts()).usingElementComparatorIgnoringFields("seq", "menuId")
                .isEqualTo(request.getMenuProducts())
        );
    }

    @Test
    void list() throws Exception {
        Product product = new Product("product1", BigDecimal.TEN);
        Product persistProduct = productDao.save(product);

        MenuProductRequest menuProduct = new MenuProductRequest(persistProduct.getId(), 10);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        MenuRequest menu = new MenuRequest("menu1", BigDecimal.TEN, persistMenuGroup.getId(),
            Arrays.asList(menuProduct));
        MenuResponse persistMenu = menuService.create(menu);

        String response = mockMvc.perform(get("/api/menus")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<MenuResponse> menusResponse = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, MenuResponse.class));

        assertThat(menusResponse).usingElementComparatorIgnoringFields("menuProducts").contains(persistMenu);
    }
}