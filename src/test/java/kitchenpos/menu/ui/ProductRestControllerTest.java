package kitchenpos.menu.ui;

import java.util.List;
import kitchenpos.menu.application.dto.request.ProductCreateRequest;
import kitchenpos.menu.application.dto.response.ProductResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate_insert_data.sql")
class ProductRestControllerTest {

    @Autowired
    ProductRestController productRestController;

    @DisplayName("POST {{host}}/api/products")
    @Test
    void product_create() {
        //given
        final ProductCreateRequest request = MenuRequestFixture.productCreateRequest();

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
