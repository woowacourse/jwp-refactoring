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
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    private MenuDao menuDao;

    @Test
    void create() throws Exception {
        Product product = new Product();
        product.setName("product1");
        product.setPrice(BigDecimal.ONE);
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(persistProduct.getId());
        menuProduct.setQuantity(10);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu request = new Menu();
        request.setPrice(BigDecimal.TEN);
        request.setName("menu1");
        request.setMenuGroupId(persistMenuGroup.getId());
        request.setMenuProducts(Arrays.asList(menuProduct));

        String response = mockMvc.perform(post("/api/menus")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Menu menuResponse = mapper.readValue(response, Menu.class);

        assertAll(
            () -> assertThat(menuResponse.getPrice().longValue()).isEqualTo(request.getPrice().longValue()),
            () -> assertThat(menuResponse).isEqualToComparingOnlyGivenFields(request, "name", "menuGroupId"),
            () -> assertThat(menuResponse.getMenuProducts()).usingElementComparatorIgnoringFields("seq", "menuId")
                .isEqualTo(request.getMenuProducts())
        );
    }

    @Test
    void list() throws Exception {
        Product product = new Product();
        product.setName("product1");
        product.setPrice(BigDecimal.ONE);
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(persistProduct.getId());
        menuProduct.setQuantity(10);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();
        menu.setPrice(BigDecimal.TEN);
        menu.setName("menu1");
        menu.setMenuGroupId(persistMenuGroup.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        String response = mockMvc.perform(get("/api/menus")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Menu> menusResponse = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, Menu.class));

        assertThat(menusResponse).usingElementComparatorIgnoringFields("menuProducts").contains(persistMenu);
    }
}