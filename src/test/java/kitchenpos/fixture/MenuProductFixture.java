package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.response.MenuProductResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class MenuProductFixture extends DefaultFixture {

    public MenuProductFixture(RequestBuilder requestBuilder) {
        super(requestBuilder);
    }

    public MenuProductRequest 메뉴_상품_생성_요청(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public List<MenuProductRequest> 메뉴_상품_요청_리스트_생성(MenuProductRequest... menuProductRequests) {
        return Arrays.asList(menuProductRequests);
    }
}
