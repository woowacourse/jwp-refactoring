package kitchenpos.domain;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {
        @Test
        @DisplayName("상품을 생성할 때 가격이 음수면 예외가 발생한다")
        void create_fail() {
            assertThatThrownBy(() -> new Product("떡볶이", new Price(BigDecimal.valueOf(-1))))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("금액이 없거나 음수입니다.");
        }

        @Test
        @DisplayName("상품을 생성할 때 가격이 존재하지 않으면 예외가 발생한다")
        void create_fail2() {
            assertThatThrownBy(() -> new Product("떡볶이", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품 금액이 필요합니다.");
        }

        @Test
        @DisplayName("상품을 생성할 때 이름이 존재하지 않으면 예외가 발생한다")
        void create_fail3() {
            assertThatThrownBy(() -> new Product("  ", new Price(BigDecimal.TEN)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 이름이 존재하지 않습니다.");
        }
    }

    @Test
    @DisplayName("수량을 받아서 가격을 계산할 수 있다")
    void calculatePrice() {
        //given
        final Product product = new Product("연어", new Price(BigDecimal.valueOf(1000)));

        //when
        final Price price = product.calculatePrice(5);

        //then
        assertThat(price.equalsWith(5000)).isTrue();
    }
}
