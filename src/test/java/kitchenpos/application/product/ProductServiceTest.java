package kitchenpos.application.product;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록에 성공한다.")
    void succeedInRegisteringProduct() {
        //given
        ProductRequest request = new ProductRequest("치킨", BigDecimal.valueOf(17000L));

        //when
        Product savedProduct = productService.create(request);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedProduct.getId()).isNotNull();
            softly.assertThat(savedProduct.getName()).isEqualTo("치킨");
        });
    }

    @Test
    @DisplayName("상품 가격이 0원 미만일 경우 예외가 발생한다.")
    void failToRegisterProduct() {
        //given
        ProductRequest productRequest = new ProductRequest("치킨", BigDecimal.valueOf(-1));

        //when & then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다.")
    void succeedInSearchingProductList() {
        //given
        ProductRequest productA = new ProductRequest("치킨", BigDecimal.valueOf(10000L));
        ProductRequest productB = new ProductRequest("피자", BigDecimal.valueOf(15000L));

        productService.create(productA);
        productService.create(productB);

        //when & then
        assertThat(productService.list()).hasSize(2);
    }
}
