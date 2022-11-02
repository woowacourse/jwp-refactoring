package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends ApplicationTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() {
        Product product = 상품_생성(new Product("상품1", BigDecimal.valueOf(17_000)));
        Long menuGroupId = 메뉴그룹_생성(new MenuGroup("메뉴그룹1"));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1, BigDecimal.valueOf(17_000));

        Long menuId1 = 메뉴_생성(Menu.create("해장 세트", BigDecimal.valueOf(15_000), menuGroupId, List.of(menuProduct)));
        Long menuId2 = 메뉴_생성(Menu.create("아침 세트", BigDecimal.valueOf(9_000), menuGroupId, List.of(menuProduct)));

        List<MenuResponse> menus = menuService.list();

        assertThat(menus).extracting(MenuResponse::getId, MenuResponse::getName, p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(menuId1, "해장 세트", 15_000),
                        tuple(menuId2, "아침 세트", 9_000)
                );
    }
}
