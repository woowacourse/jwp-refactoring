package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.Fixtures;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends ServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    Fixtures fixtures;

    @Nested
    class 메뉴_등록 {

        @Test
        void 메뉴를_등록한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("치킨메뉴");
            Product product = fixtures.상품_저장("치킨", 18_000L);

            Menu menu = new Menu();
            menu.setName("한마리 메뉴");
            menu.setPrice(BigDecimal.valueOf(19_000));
            menu.setMenuGroup(menuGroup);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(2);

            menu.setMenuProducts(List.of(menuProduct));

            // when
            Menu result = menuService.create(menu);

            // then
            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(menu);
        }

        @Test
        void 메뉴그룹이_존재하지_않으면_예외가_발생한다() {
            // given
            Product product = fixtures.상품_저장("치킨", 18_000L);

            Menu menu = new Menu();
            menu.setName("한마리 메뉴");
            menu.setPrice(BigDecimal.valueOf(19_000));
            menu.setMenuGroup(null);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(2);

            menu.setMenuProducts(List.of(menuProduct));

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Nested
        class 메뉴_가격 {

            @ParameterizedTest
            @ValueSource(longs = {-1, -12312, -999})
            void 메뉴_가격이_음수인_경우_에러가_발생한다(Long value) {
                MenuGroup menuGroup = fixtures.메뉴_그룹_저장("치킨메뉴");
                Product product = fixtures.상품_저장("치킨", 18_000L);

                Menu menu = new Menu();
                menu.setName("한마리 메뉴");
                menu.setPrice(BigDecimal.valueOf(value));
                menu.setMenuGroup(menuGroup);

                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProduct(product);
                menuProduct.setQuantity(2);

                menu.setMenuProducts(List.of(menuProduct));

                // when, then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_가격이_null인_경우_에러가_발생한다() {
                // given
                MenuGroup menuGroup = fixtures.메뉴_그룹_저장("치킨메뉴");
                Product product = fixtures.상품_저장("치킨", 18_000L);

                Menu menu = new Menu();
                menu.setName("한마리 메뉴");
                menu.setPrice(null);
                menu.setMenuGroup(menuGroup);

                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProduct(product);
                menuProduct.setQuantity(2);

                menu.setMenuProducts(List.of(menuProduct));

                // when, then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void 메뉴_가격이_메뉴_상품의_총_가격보다_큰_경우_예외가_발생한다() {
                // given
                MenuGroup menuGroup = fixtures.메뉴_그룹_저장("치킨메뉴");
                Product product = fixtures.상품_저장("치킨", 18_000L);

                Menu menu = new Menu();
                menu.setName("한마리 메뉴");
                menu.setPrice(BigDecimal.valueOf(19_000));
                menu.setMenuGroup(menuGroup);

                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProduct(product);
                menuProduct.setQuantity(1);

                menu.setMenuProducts(List.of(menuProduct));

                // when, then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Test
    void 모든_메뉴_목록을_불러온다() {
        // given
        MenuGroup menuGroup = fixtures.메뉴_그룹_저장("치킨메뉴");
        Product product = fixtures.상품_저장("치킨", 18_000L);
        Menu menu = fixtures.메뉴_저장(menuGroup, "치킨 + 콜라", 18_000L);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus.get(0)).isEqualTo(menu);
    }

}
