package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static kitchenpos.fixture.MenuProductFixture.REQUEST.후라이드_치킨_1마리_요청;
import static kitchenpos.fixture.ProductFixture.PRODUCT;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
public class MenuProductMapperTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private MenuProductMapper menuProductMapper;

    @Nested
    class toMenuProduct_메소드는_ {

        @Test
        void 메뉴_상품_생성_요청을_메뉴_상품으로_변환한다() {
            // given
            MenuProductRequest request = 후라이드_치킨_1마리_요청();
            given(productRepository.findById(anyLong()))
                    .willReturn(Optional.of(PRODUCT.후라이드_치킨()));

            // when
            MenuProduct menuProduct = menuProductMapper.toMenuProduct(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(menuProduct.getSeq()).isNull();
                softly.assertThat(menuProduct.getProduct()).isNotNull();
                softly.assertThat(menuProduct.getQuantity()).isEqualTo(request.getQuantity());
            });
        }

        @Test
        void 상품이_존재하지_않으면_예외() {
            // given
            given(productRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> menuProductMapper.toMenuProduct(후라이드_치킨_1마리_요청()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
