package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.response.MenuProductResponse;

import java.util.Arrays;
import java.util.List;

public class MenuProductFixture {

    public MenuProductRequest 메뉴_상품_생성_요청(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public MenuProduct 메뉴_상품_생성(Long seq, Menu menu, Product product, Long quantity) {
        return MenuProduct.create(seq, menu, product, quantity);
    }

    public List<MenuProductRequest> 메뉴_상품_요청_리스트_생성(MenuProductRequest... menuProductRequests) {
        return Arrays.asList(menuProductRequests);
    }

    public List<MenuProductResponse> 메뉴_상품_리스트_생성(MenuProductResponse... menuProductResponses) {
        return Arrays.asList(menuProductResponses);
    }
}
