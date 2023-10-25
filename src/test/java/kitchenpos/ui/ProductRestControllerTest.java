package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import java.util.List;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.application.support.RequestFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class ProductRestControllerTest {

    @Autowired
    ProductRestController productRestController;

    @DisplayName("POST {{host}}/api/products")
    @Test
    void product_create() {
        //given
        final ProductCreateRequest request = RequestFixture.productCreateRequest();

        //when
        final ResponseEntity<ProductResponse> response = productRestController.create(request);

        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().getName()).isEqualTo(request.getName());
                    soft.assertThat(response.getBody().getId()).isNotNull();
                }
        );
    }

    @DisplayName("GET {{host}}/api/products")
    @Test
    void product_list() {
        //given

        //when
        final ResponseEntity<List<ProductResponse>> response = productRestController.list();
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().size()).isEqualTo(6);
                }
        );
    }
}
