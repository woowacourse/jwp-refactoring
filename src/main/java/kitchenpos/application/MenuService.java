package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuCreateRequest request) {
        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroupRepository.getById(request.getMenuGroupId())
        );
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProductCreateRequest> menuProductCreateRequestList = request.getMenuProducts();
        for (final MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequestList) {
            final MenuProduct menuProduct =
                    new MenuProduct(
                            savedMenu,
                            productRepository.getById(menuProductCreateRequest.getProductId()),
                            menuProductCreateRequest.getQuantity()
                    );
            savedMenu.addMenuProduct(menuProduct);
        }
        return savedMenu;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
