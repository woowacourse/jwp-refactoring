package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("상품 목록을 조회한다.")
    @Test
    public void list() {
        webTestClient.get().uri("/api/products")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Product.class);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    public void create() {
        Product product = new Product("테스트 상품", new BigDecimal(1_000));
        webTestClient.post().uri("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(product), Product.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Product.class)
            .consumeWith(response -> {
                Product responseProduct = response.getResponseBody();
                assertThat(responseProduct.getName()).isEqualTo(product.getName());
                assertThat(responseProduct.getPrice().toBigInteger()).isEqualTo(product.getPrice().toBigInteger());
            });
    }


}
