package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_MENU_GROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_PRODUCT_EXCEPTION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final JpaMenuRepository menuRepository;
    private final JpaMenuGroupRepository menuGroupRepository;
    private final JpaProductRepository productRepository;

    public MenuService(final JpaMenuRepository menuRepository, final JpaMenuGroupRepository menuGroupRepository,
                       final JpaProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuRequest request) {
        validMenuProduct(request.getMenuProducts());
        validMenuGroup(request.getMenuGroupId());
        final List<MenuProduct> menuProducts = getMenuProducts(request);
        return menuRepository.save(request.toMenu(menuProducts));
    }

    private List<MenuProduct> getMenuProducts(final MenuRequest request) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            menuProducts.add(new MenuProduct(menuProductRequest.getProductId(), menuProductRequest.getQuantity(),
                    calculateMenuProductPrice(menuProductRequest)));
        }
        return menuProducts;
    }

    private BigDecimal calculateMenuProductPrice(final MenuProductRequest menuProductRequest) {
        final Product product = getProduct(menuProductRequest);
        return product.getPrice()
                .multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
    }

    private Product getProduct(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_PRODUCT_EXCEPTION));
    }

    private void validMenuProduct(final List<MenuProductRequest> menuProductRequests) {
        for (MenuProductRequest menuProduct : menuProductRequests) {
            validProduct(menuProduct.getProductId());
        }
    }

    private void validProduct(final Long id) {
        if (!productRepository.existsById(id)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_PRODUCT_EXCEPTION);
        }
    }

    //: todo 현재 등록된 메뉴 그룹이 있는가? 이게 여기 있는게 맞는가 ?
    private void validMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_MENU_GROUP_EXCEPTION);
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
