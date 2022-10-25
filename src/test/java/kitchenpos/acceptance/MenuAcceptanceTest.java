package kitchenpos.acceptance;

import static kitchenpos.DomainFixtures.라면_메뉴;
import static kitchenpos.DomainFixtures.맛있는_라면;
import static kitchenpos.DomainFixtures.면_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTest {

    private Product 라면_제품;
    private MenuGroup 메뉴_그룹;
    private Menu 라면_메뉴;

    @BeforeEach
    void setData() {
        라면_제품 = testRestTemplate.postForObject("http://localhost:" + port + "/api/products",
                맛있는_라면(), Product.class);

        메뉴_그룹 = testRestTemplate.postForObject("http://localhost:" + port + "/api/menu-groups",
                면_메뉴_그룹(), MenuGroup.class);

        MenuProduct menuProduct = new MenuProduct(null, 라면_제품.getId(), 1);
        List<MenuProduct> 메뉴_그룹들 = new ArrayList<>();
        메뉴_그룹들.add(menuProduct);

        라면_메뉴 = 라면_메뉴();
        라면_메뉴.setMenuGroupId(메뉴_그룹.getId());
        라면_메뉴.setMenuProducts(메뉴_그룹들);
    }

    @Test
    void 메뉴를_추가한다() {
        Menu menu = testRestTemplate.postForObject("http://localhost:" + port + "/api/menus", 라면_메뉴, Menu.class);

        assertThat(menu.getName()).isEqualTo("라면");
        assertThat(menu.getId()).isNotZero();
    }

    @Test
    void 메뉴들을_조회한다() {
        testRestTemplate.postForObject("http://localhost:" + port + "/api/menus", 라면_메뉴, Menu.class);

        List<Menu> menus = Arrays.asList(
                testRestTemplate.getForObject("http://localhost:" + port + "/api/menus", Menu[].class));

        assertThat(menus).hasSize(1);
    }
}
