package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuRequest request;

    @BeforeEach
    void setMenu() {
        Product product = new Product("product1", BigDecimal.ONE);
        Product persistProduct = productDao.save(product);

        MenuProductRequest menuProduct = new MenuProductRequest(persistProduct.getId(), 10);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        request = new MenuRequest("menu1", BigDecimal.TEN, persistMenuGroup.getId(), Arrays.asList(menuProduct));
    }

    @Test
    void create() {
        MenuResponse response = menuService.create(request);

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response).isEqualToComparingOnlyGivenFields(request, "name", "menuGroupId"),
            () -> assertThat(response.getPrice().longValue()).isEqualTo(request.getPrice().longValue()),
            () -> assertThat(response.getMenuProducts().get(0).getMenuId()).isNotNull(),
            () -> assertThat(response.getMenuProducts().get(0).getQuantity())
                .isEqualTo(request.getMenuProducts().get(0).getQuantity()),
            () -> assertThat(response.getMenuProducts().get(0).getProductId())
                .isEqualTo(request.getMenuProducts().get(0).getProductId())
        );
    }

    @Test
    void list() {
        MenuResponse persistMenu = menuService.create(request);
        List<MenuResponse> menus = menuService.list();
        List<String> menuNames = menus.stream()
            .map(MenuResponse::getName)
            .collect(Collectors.toList());

        assertThat(menuNames).contains(persistMenu.getName());
    }
}