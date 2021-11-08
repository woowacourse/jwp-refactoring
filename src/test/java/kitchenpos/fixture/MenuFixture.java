package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuFixture {
    private static final String NAME = "MENU_NAME";
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final Long MENU_GROUP_ID = 1L;

    private static final Long SEQ = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    public static MenuRequest createMenuRequest() {
        return new MenuRequest(NAME, PRICE, MENU_GROUP_ID, createMenuProductRequests());
    }

    public static MenuRequest createMenuRequest(Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(NAME, PRICE, menuGroupId, menuProductRequests);
    }

    public static MenuRequest createMenuRequest(BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(NAME, price, menuGroupId, menuProductRequests);
    }

    public static MenuResponse createMenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProductResponses) {
        return new MenuResponse(id, name, price, menuGroupId, menuProductResponses);
    }

    public static MenuResponse createMenuResponse(Long id) {
        return new MenuResponse(id, NAME, PRICE, MENU_GROUP_ID, Collections.singletonList(createMenuProductResponse()));
    }

    public static MenuResponse createMenuResponse(Long id, MenuRequest request) {
        final List<MenuProductResponse> menuProductResponses = createMenuProductResponses(request.getMenuProductRequests());
        return new MenuResponse(id, request.getName(), request.getPrice(), request.getMenuGroupId(), menuProductResponses);
    }

    public static MenuProductResponse createMenuProductResponse() {
        return new MenuProductResponse(1L, SEQ, PRODUCT_ID, QUANTITY);
    }

    public static MenuProductResponse createMenuProductResponse(Long id, MenuProductRequest request) {
        return new MenuProductResponse(id, request.getSeq(), request.getProductId(), request.getQuantity());
    }

    public static List<MenuProductResponse> createMenuProductResponses(List<MenuProductRequest> requests) {
        long index = 1L;
        List<MenuProductResponse> menuProductResponses = new ArrayList<>();
        for (MenuProductRequest request : requests) {
            menuProductResponses.add(createMenuProductResponse(index++, request));
        }
        return menuProductResponses;
    }

    public static MenuProductRequest createMenuProductRequest(Long seq, Long productId, long quantity) {
        return new MenuProductRequest(seq, productId, quantity);
    }

    public static MenuProductRequest createMenuProductRequest() {
        return createMenuProductRequest(SEQ, PRODUCT_ID, QUANTITY);
    }

    public static List<MenuProductRequest> createMenuProductRequests() {
        return Collections.singletonList(createMenuProductRequest());
    }
}
