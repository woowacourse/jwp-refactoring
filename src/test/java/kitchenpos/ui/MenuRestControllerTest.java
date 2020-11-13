package kitchenpos.ui;

import static kitchenpos.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductRequest;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class MenuRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() throws Exception {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
        Product product1 = productDao.save(createProduct(null, "상품1", 100L));
        Product product2 = productDao.save(createProduct(null, "상품2", 500L));
        Product product3 = productDao.save(createProduct(null, "상품3", 1000L));
        List<MenuProduct> menuProducts = Arrays.asList(
            createMenuProductRequest(product1.getId(), 10),
            createMenuProductRequest(product2.getId(), 10),
            createMenuProductRequest(product3.getId(), 10)
        );
        Menu menuRequest = createMenuRequest("메뉴1", 0L, menuGroup.getId(), menuProducts);

        mockMvc.perform(
            post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(menuRequest))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(menuRequest.getName()))
            .andExpect(jsonPath("$.price").value(menuRequest.getPrice().longValue()))
            .andExpect(jsonPath("$.menuGroupId").value(menuRequest.getMenuGroupId()))
            .andExpect(jsonPath("$.menuProducts").isNotEmpty());
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<Menu> menus = menuDao.findAll();

        String json = mockMvc.perform(get("/api/menus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<Menu> response = objectMapper.readValue(json,
            objectMapper.getTypeFactory().constructCollectionType(List.class, Menu.class));

        assertThat(response).usingFieldByFieldElementComparator().containsAll(menus);
    }
}
