package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("메뉴 스냅샷(MenuSnapShot) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuSnapShotTest {

    @Test
    void 메뉴를_통해_생성되며_해당_시점의_메뉴의_상태를_저장한다() {
        // given
        Product product1 = new Product("말랑1", BigDecimal.valueOf(1000));
        Product product2 = new Product("말랑2", BigDecimal.valueOf(2000));
        MenuGroup menuGroup = new MenuGroup("메뉴그룹 1");
        Menu menu = new Menu(
                "말랑메뉴",
                BigDecimal.valueOf(200),
                menuGroup,
                List.of(
                        new MenuProduct(product1, 2L),
                        new MenuProduct(product2, 2L)
                )
        );

        // when
        MenuSnapShot snapShot = new MenuSnapShot(
                menuGroup.getName(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuProducts()
                        .stream()
                        .map(it -> new MenuProductSnapShot(
                                it.getProduct().getName(),
                                it.getProduct().getPrice(),
                                it.getQuantity())
                        ).collect(Collectors.toList())
        );

        // then
        ReflectionTestUtils.setField(menu, "name", "변경된 이름");
        ReflectionTestUtils.setField(menuGroup, "name", "변경된 메뉴그룹 이름");
        ReflectionTestUtils.setField(product1, "name", "변경된 상품1 이름");
        assertThat(snapShot.getName()).isEqualTo("말랑메뉴");
        assertThat(snapShot.getMenuGroupName()).isEqualTo("메뉴그룹 1");
        assertThat(snapShot.getMenuProducts().get(0).getName()).isEqualTo("말랑1");
    }
}
