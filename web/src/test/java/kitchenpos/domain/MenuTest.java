package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu.Builder;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("Menu 객체 정상 생성")
    void create_success() {
        // given
        String name = "후라이드";
        MenuProduct menuProduct = MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity(ProductFixture.FRIED_CHICKEN.toEntity());
        BigDecimal price = BigDecimal.valueOf(menuProduct.getPrice().longValue() * menuProduct.getQuantity());

        // when
        Builder builder = new Builder()
            .name(name)
            .price(price)
            .menuGroupId(1L)
            .menuProducts(List.of(menuProduct));

        // then
        assertDoesNotThrow(builder::build);
    }
}
