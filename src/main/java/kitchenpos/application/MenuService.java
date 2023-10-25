package kitchenpos.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuProductCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.mapper.MenuMapper;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        final Menu menu = saveMenu(request);
        menuProductRepository.saveAll(menu.getMenuProducts());

        return MenuMapper.toMenuResponse(menu);
    }

    private Menu saveMenu(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 menu group 입니다."));
        final Menu menu = MenuMapper.toMenu(request, menuGroup, convertToMenuProducts(request));

        return menuRepository.save(menu);
    }

    private List<MenuProduct> convertToMenuProducts(final MenuCreateRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
                .map(this::convertToMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct convertToMenuProduct(final MenuProductCreateRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 product 입니다."));

        return new MenuProduct(null, product, request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> readAll() {
        final List<Menu> menus = menuRepository.findAll();

        return MenuMapper.toMenuResponses(menus);
    }
}
