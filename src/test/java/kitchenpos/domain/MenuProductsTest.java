package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuProductsTest {
    
    private Product product1;
    private Product product2;
    private MenuGroup menuGroup;
    
    @BeforeEach
    void setUp() {
        product1 = Product.create("후라이드", BigDecimal.valueOf(16000));
        product2 = Product.create("양념치킨", BigDecimal.valueOf(16000));
        menuGroup = MenuGroup.create("두마리메뉴");
    }
    
    @DisplayName("메뉴 상품들을 생성할 수 있다.")
    @Test
    void menuProducts() {
        // given
        Menu menu = Menu.create("두마리메뉴 - 후1양1", BigDecimal.valueOf(32000L), menuGroup);
        
        MenuProduct menuProduct1 = MenuProduct.create(menu, product1, 1L);
        MenuProduct menuProduct2 = MenuProduct.create(menu, product2, 1L);
        
        // then
        assertDoesNotThrow(() -> MenuProducts.create(List.of(menuProduct1, menuProduct2)));
    }
    
    @DisplayName("메뉴 가격이 메뉴 상품들의 가격 합보다 크면 True를 반환한다.")
    @ParameterizedTest
    @ValueSource(longs = {32001L, 33000L})
    void isInvalidPrice_True(long invalidPrice) {
        // given
        BigDecimal price = BigDecimal.valueOf(invalidPrice);
        Menu menu = Menu.create("두마리메뉴 - 후1양1", price, menuGroup);
        
        MenuProduct menuProduct1 = MenuProduct.create(menu, product1, 1L);
        MenuProduct menuProduct2 = MenuProduct.create(menu, product2, 1L);

        MenuProducts menuProducts = MenuProducts.create(List.of(menuProduct1, menuProduct2));
        
        // when & then
        assertThat(menuProducts.isInvalidPrice(price)).isTrue();
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 가격 합보다 같거나 작으면 False를 반환한다.")
    @ParameterizedTest
    @ValueSource(longs = {32000L, 31000L})
    void isInvalidPrice_False(long validPrice) {
        // given
        BigDecimal price = BigDecimal.valueOf(validPrice);
        Menu menu = Menu.create("두마리메뉴 - 후1양1", price, menuGroup);

        MenuProduct menuProduct1 = MenuProduct.create(menu, product1, 1L);
        MenuProduct menuProduct2 = MenuProduct.create(menu, product2, 1L);

        MenuProducts menuProducts = MenuProducts.create(List.of(menuProduct1, menuProduct2));

        // when & then
        assertThat(menuProducts.isInvalidPrice(price)).isFalse();
    }
}
