package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Nested
    class create_메소드는 {

        @Nested
        class 가격이_null이_입력될_경우 {

            private final Menu menu = new Menu("파닭", null, 1L, new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
            }
        }

        @Nested
        class 가격이_음수가_입력될_경우 {

            private final Menu menu = new Menu("파닭", BigDecimal.valueOf(-1), 1L, new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
            }
        }

        @Nested
        class 없는_메뉴_그룹의_id가_입력될_경우 {

            private final Menu menu = new Menu("파닭", BigDecimal.valueOf(10000), -1L, new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 메뉴 그룹의 id입니다.");
            }
        }

        @Nested
        class 입력된_메뉴상품의_상품id가_존재하지_않는_경우 {

            private final Menu menu = new Menu("파닭", BigDecimal.valueOf(10000), 1L, createErrorMenuProducts());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            private List<MenuProduct> createErrorMenuProducts() {
                final List<MenuProduct> menuProducts = new ArrayList<>();
                menuProducts.add(new MenuProduct(0L, 1));
                return menuProducts;
            }
        }
    }
}
