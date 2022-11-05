package kitchenpos.product.presentation;

import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.product.application.dto.request.ProductCommand;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.presentation.dto.ProductRequest;
import kitchenpos.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class ProductRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("Product를 생성한다.")
    void create() throws Exception {
        ProductResponse productResponse = new ProductResponse(1L, PRODUCT1_NAME, PRODUCT1_PRICE);
        given(productService.create(any(ProductCommand.class))).willReturn(productResponse);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductRequest(PRODUCT1_NAME, PRODUCT1_PRICE))))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/products/1"))
                .andDo(print());
    }

    @Test
    @DisplayName("모든 Product를 조회한다.")
    void list() throws Exception {
        List<ProductResponse> productResponses = List.of(new ProductResponse(1L, PRODUCT1_NAME, PRODUCT1_PRICE),
                new ProductResponse(2L, PRODUCT2_NAME, PRODUCT2_PRICE));

        given(productService.list()).willReturn(productResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(productResponses)));
    }
}
