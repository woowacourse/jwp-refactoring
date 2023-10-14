package kitchenpos.application;

import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록에 성공한다.")
    void succeedInRegisteringProduct() {
        //given
        String productName = "치킨";
        Product product = new Product();
        product.setName(productName);
        product.setPrice(BigDecimal.valueOf(17000L));

        //when
        Product savedProduct = productService.create(product);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedProduct.getId()).isNotNull();
            softly.assertThat(savedProduct.getName()).isEqualTo(productName);
        });
    }

    @Test
    @DisplayName("상품 가격이 0원 미만일 경우 예외가 발생한다.")
    void failToRegisterProduct() {
        //given
        Product product = new Product();
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(-1));

        //when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다.")
    void succeedInSearchingProductList() {
        //given
        Product productA = new Product();
        productA.setName("치킨");
        productA.setPrice(BigDecimal.valueOf(10000L));

        Product productB = new Product();
        productB.setName("치킨");
        productB.setPrice(BigDecimal.valueOf(10000L));

        productService.create(productA);
        productService.create(productB);

        //when
        assertThat(productService.list()).hasSize(2);
    }
}
