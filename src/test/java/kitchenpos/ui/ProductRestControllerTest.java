package kitchenpos.ui;

import static kitchenpos.support.DomainFixture.뿌링클;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.application.product.ProductService;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.support.ControllerTest;
import kitchenpos.support.SimpleMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductRestControllerTest extends ControllerTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품_생성을_요청한다() throws Exception {
        // given
        final var request = new ProductCreateRequest(
                뿌링클.getName(),
                뿌링클.getPrice().intValue()
        );
        final var responseA = new ProductResponse(1L, 뿌링클.getName(), 뿌링클.getPrice());
        given(productService.create(any(ProductCreateRequest.class))).willReturn(responseA);

        // when
        final var response = SimpleMockMvc.post(mockMvc, "/api/products", request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + 1L));
    }

    @Test
    void 필요한_요청정보가_누락되면_BAD_REQUEST를_응답한다() throws Exception {
        // given
        final var request = new ProductCreateRequest(
                null,
                뿌링클.getPrice().intValue()
        );

        // when
        final var response = SimpleMockMvc.post(mockMvc, "/api/products", request);

        // then
        response.andExpect(status().isBadRequest());
    }
}
