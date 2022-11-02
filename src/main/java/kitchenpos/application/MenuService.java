package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.MenusResponse;
import kitchenpos.exceptions.MenuGroupNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final Menu menu = convertToMenu(menuRequest);
        validateMenuGroupExist(menuRequest.getMenuGroupId());
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private Menu convertToMenu(final MenuRequest menuRequest) {
        final List<Product> products = getProducts(menuRequest);
        return menuRequest.toEntity(products);
    }

    private List<Product> getProducts(final MenuRequest menuRequest) {
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        final List<Long> productIds = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        return productRepository.findByIds(productIds);
    }

    private void validateMenuGroupExist(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupNotExistException();
        }
    }

    @Transactional(readOnly = true)
    public MenusResponse list() {
        final List<Menu> menus = menuRepository.findAll();

        return MenusResponse.from(menus);
    }
}
