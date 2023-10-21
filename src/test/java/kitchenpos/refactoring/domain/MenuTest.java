package kitchenpos.refactoring.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuTest {

    private List<Product> products;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        products = List.of(
                new Product(1L, "Chicken", new Price(BigDecimal.valueOf(1000))),
                new Product(2L, "Beef", new Price(BigDecimal.valueOf(1500)))
        );

        menuGroup = new MenuGroup("레오가 추천하는 저녁 메뉴");

        menuProducts = List.of(
                new MenuProduct(1L, 2L),
                new MenuProduct(2L, 1L)
        );
    }

    @Nested
    class create {

        @Test
        void success() {
            // given
            String name = "저녁 특선";
            Price menuPrice = new Price(BigDecimal.valueOf(2500)); // total of product prices

            // when
            Menu menu = Menu.create(
                    name,
                    menuPrice,
                    menuGroup,
                    menuProducts,
                    products
            );

            // then
            assertThat(menu).isNotNull();
            assertThat(menu.getName()).isEqualTo(name);
            assertThat(menu.getPrice()).isEqualTo(menuPrice);
            assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId());
        }


        @Test
        void shouldThrowExceptionForInvalidPrice() {
            // given
            Price invalidMenuPrice = new Price(BigDecimal.valueOf(5000));

            // when
            // then
            assertThatThrownBy(() -> Menu.create("저녁 특선", invalidMenuPrice, menuGroup, menuProducts, products))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }
}
