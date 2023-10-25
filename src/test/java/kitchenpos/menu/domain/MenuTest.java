package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.test.fixture.MenuFixture;
import kitchenpos.test.fixture.MenuGroupFixture;
import kitchenpos.test.fixture.ProductFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Nested
    class 메뉴_상품_목록_수정_시 {

        @ParameterizedTest
        @ValueSource(longs = {4, 3})
        void 메뉴_가격이_상품_가격보다_작거나_같으면_상품_목록_수정을_성공한다(long quantity) {
            //given
            MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹("중식");
            Menu menu = MenuFixture.메뉴("짜장면 세트", BigDecimal.valueOf(15000), menuGroup);
            Product product = ProductFixture.상품("짜장면", BigDecimal.valueOf(5000));
            List<MenuProduct> menuProducts = List.of(new MenuProduct(menu, product, quantity));

            //when
            menu.updateProducts(menuProducts);

            //then
            assertSoftly(softly -> {
                softly.assertThat(menu.getMenuProducts()).hasSize(1);
                softly.assertThat(menu.getMenuProducts().get(0).getMenu()).isEqualTo(menu);
                softly.assertThat(menu.getMenuProducts().get(0).getProduct().getName()).isEqualTo(product.getName());
                softly.assertThat(menu.getMenuProducts().get(0).getProduct().getPrice())
                        .isEqualByComparingTo(product.getPrice());
                softly.assertThat(menu.getMenuProducts().get(0).getQuantity()).isEqualTo(quantity);
            });
        }

        @Test
        void 메뉴_가격이_상품_가격보다_크면_예외를_던진다() {
            //given
            MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹("중식");
            Menu menu = MenuFixture.메뉴("짜장면 세트", BigDecimal.valueOf(15000), menuGroup);
            Product product = ProductFixture.상품("짜장면", BigDecimal.valueOf(5000));
            List<MenuProduct> menuProducts = List.of(new MenuProduct(menu, product, 2));

            //when, then
            assertThatThrownBy(() -> menu.updateProducts(menuProducts))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격이 상품 가격의 합보다 클 수 없습니다.");
        }
    }
}
