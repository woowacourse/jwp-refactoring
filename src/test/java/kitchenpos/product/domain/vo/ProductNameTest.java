package kitchenpos.product.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.product.domain.vo.ProductName;
import kitchenpos.test.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
class ProductNameTest {

    @Nested
    class 상품명_생성_시 {

        @Test
        void 정상적인_상품명이라면_생성에_성공한다() {
            //given
            String name = "쌀국수";

            //when
            ProductName productName = new ProductName(name);

            //then
            assertThat(productName.getName()).isEqualTo(name);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상품명이_존재하지_않거나_공백이라면_예외를_던진다(String name) {
            //given, when, then
            assertThatThrownBy(() -> new ProductName(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품명이 존재하지 않거나 공백입니다.");
        }

        @Test
        void 상품명_길이가_유효하지_않으면_예외를_던진다() {
            //given
            String name = RandomStringUtils.random(256);

            //when, then
            assertThatThrownBy(() -> new ProductName(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품명 길이가 유효하지 않습니다.");
        }
    }
}
