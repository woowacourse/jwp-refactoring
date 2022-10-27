package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 api")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        // given
        final Product savedProduct = dataSupport.saveProduct("참치마요", new BigDecimal(4000));
        final MenuGroup savedMenuGroup = dataSupport.saveMenuGroup("할인 상품");
        final BigDecimal discountedPrice = savedProduct.getPrice().add(new BigDecimal(-500));

        // when
        final Menu request = RequestBuilder.ofMenu(savedMenuGroup, Arrays.asList(savedProduct), discountedPrice);
        final ValidatableResponse response = post("/api/menus", request);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given, when
        final ValidatableResponse response = get("/api/menus");

        // then
        response.statusCode(HttpStatus.OK.value());
    }
}
