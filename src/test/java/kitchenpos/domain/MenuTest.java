package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.factory.MenuFactory;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.ProductFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    private Menu menu;

    private Products products;

    @BeforeEach
    void setUp() {
        MenuProduct menuProduct = MenuProductFactory.builder()
            .seq(1L)
            .menuId(1L)
            .productId(1L)
            .quantity(2L)
            .build();

        menu = MenuFactory.builder()
            .id(1L)
            .name("후라이드+후라이드")
            .price(new BigDecimal(19000))
            .menuProducts(menuProduct)
            .build();

        Product product = ProductFactory.builder()
            .id(1L)
            .name("강정치킨")
            .price(new BigDecimal(17000))
            .build();

        products = new Products(product);
    }

    @DisplayName("Menu 객체가 관련 Product 들의 금액 합보다 작은 양수의 금액을 가지고 있는지 확인한다")
    @Test
    void comparePrice() {
        // when
        ThrowingCallable throwingCallable = () -> menu.comparePrice(products);

        // then
        assertThatCode(throwingCallable)
            .doesNotThrowAnyException();
    }

    @DisplayName("Menu 객체 금액 검증에 실패한다 - price 가 음수인 경우")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @ValueSource(ints = {-1, -100, -1000000})
    void comparePriceFail_whenPriceIsNegative(int value) {
        // given
        menu = MenuFactory.copy(menu)
            .price(new BigDecimal(value))
            .build();

        // when
        ThrowingCallable throwingCallable = () -> menu.comparePrice(products);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 객체 금액 검증에 실패한다 - price 가 Product 들의 금액 합보다 큰 경우")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @ValueSource(ints = {34001, 35000, 100000})
    void comparePriceFail_whenPriceIsGreater(int value) {
        // given
        menu = MenuFactory.copy(menu)
            .price(new BigDecimal(value))
            .build();

        // when
        ThrowingCallable throwingCallable = () -> menu.comparePrice(products);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
