package kitchenpos.menu.presentation;

import io.restassured.RestAssured;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.menu.presentation.dto.MenuCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class MenuRestControllerTest extends ControllerTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void Menu를_생성하면_201을_반환한다() {
        // given
        final Product product = productRepository.save(new Product("디노 초코 케이크", new BigDecimal(25000)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("디노 디저트"));
        final var 요청_준비 = RestAssured.given()
                .body(new MenuCreateRequest("디노 케이크", new BigDecimal(25000),
                        menuGroup.getId(), List.of(product.getId()), List.of(1)))
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .post("/api/menus");

        // then
        응답.then().assertThat().statusCode(CREATED.value());
    }

    @Test
    void Menu를_조회하면_200을_반환한다() {
        // given
        final var 요청_준비 = RestAssured.given()
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .get("/api/menus");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }
}
