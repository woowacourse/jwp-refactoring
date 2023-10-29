package kitchenpos.domain;

import static kitchenpos.fixture.ProductFixture.후추_칰힌_가격_책정;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;


@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    void price가_null_인_경우_상품_저장에_실패한다() {
        // expected
        assertThatThrownBy(() -> 후추_칰힌_가격_책정(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_음수인_경우_상품_저장에_실패한다() {
        // expected
        assertThatThrownBy(() -> 후추_칰힌_가격_책정(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
