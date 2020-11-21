package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.verifier.MenuGroupVerifier;
import kitchenpos.domain.verifier.MenuVerifier;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupVerifier menuGroupVerifier;
    private final MenuVerifier menuVerifier;

    public MenuService(MenuRepository menuRepository, MenuProductRepository menuProductRepository,
        MenuGroupVerifier menuGroupVerifier, MenuVerifier menuVerifier) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupVerifier = menuGroupVerifier;
        this.menuVerifier = menuVerifier;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
            .map(req -> req.toEntity(null))
            .collect(Collectors.toList());
        menuGroupVerifier.verify(request.getMenuGroupId());
        menuVerifier.verify(request.getProductIds(), menuProducts, request.getPrice());

        final Menu savedMenu = menuRepository.save(request.toEntity());
        final Long menuId = savedMenu.getId();

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenuId(menuId);
        }
        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
