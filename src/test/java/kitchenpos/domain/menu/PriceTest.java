package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.UpdateGuestNumberDto;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void ofMenu_메서드는_개별_상품을_구매했을_때의_가격보다_메뉴의_가격이_높은_경우_예외를_발생시킨다() {
        BigDecimal menuPrice = BigDecimal.valueOf(1001);
        List<ProductQuantity> productQuantities = List.of(
                new ProductQuantity(new Product("상품명", BigDecimal.valueOf(100)), 10));

        assertThatThrownBy(() -> Price.ofMenu(menuPrice, productQuantities))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
