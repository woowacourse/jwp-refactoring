package kitchenpos.integration;

import static kitchenpos.integration.api.texture.ProductTexture.강정치킨;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.integration.api.ProductApi;
import kitchenpos.integration.api.texture.ProductTexture;
import kitchenpos.integration.utils.MockMvcResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private ProductApi productApi;

    @Test
    @DisplayName("상품 등록 성공")
    public void 상품_등록() {
        // when
        final MockMvcResponse<Product> result = productApi.상품_등록(강정치킨);

        // then
        final List<Product> response = productApi.상품_검색().getContent();

        assertThat(response).extracting(Product::getName)
            .contains(강정치킨.getProduct().getName());
        assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("상품 등록 실패 - 가격 미등록")
    public void exception() {
        // when
        final MockMvcResponse<Product> result = productApi.상품_등록(new Product("강정치킨", null));

        // then
        assertThat(result.getErrorMessage()).isEqualTo("등록하는 가격이 비어있습니다.");
        assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


}
