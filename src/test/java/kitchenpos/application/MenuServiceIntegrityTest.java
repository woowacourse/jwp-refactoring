package kitchenpos.application;

import kitchenpos.SpringBootTestSupport;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static kitchenpos.MenuFixture.*;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.ProductFixture.createProduct2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class MenuServiceIntegrityTest extends SpringBootTestSupport {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup1 = save(createMenuGroup1());
        MenuGroup menuGroup2 = save(createMenuGroup2());
        Product product1 = save(createProduct1());
        Product product2 = save(createProduct2());
        Menu menu1 = save(createMenu1(menuGroup1, Collections.singletonList(product1)));
        Menu menu2 = save(createMenu2(menuGroup2, Collections.singletonList(product2)));

        List<MenuResponse> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0).getId()).isEqualTo(menu1.getId()),
                () -> assertThat(actual.get(0).getMenuProducts()).hasSize(1),
                () -> assertThat(actual.get(1).getId()).isEqualTo(menu2.getId()),
                () -> assertThat(actual.get(0).getMenuProducts()).hasSize(1)
        );
    }
}
