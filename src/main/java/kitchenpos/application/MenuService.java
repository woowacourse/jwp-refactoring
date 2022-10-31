package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.mapper.MenuDtoMapper;
import kitchenpos.dto.mapper.MenuMapper;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuMapper menuMapper;
    private final MenuDtoMapper menuDtoMapper;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuMapper menuMapper, final MenuDtoMapper menuDtoMapper,
                       final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuMapper = menuMapper;
        this.menuDtoMapper = menuDtoMapper;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        validateMenuGroupExists(menuCreateRequest);
        List<MenuProduct> menuProducts = createMenuProducts(menuCreateRequest);
        Menu menu = menuMapper.toMenu(menuCreateRequest, menuProducts);
        return menuDtoMapper.toMenuResponse(menuRepository.save(menu));
    }

    private void validateMenuGroupExists(final MenuCreateRequest menuCreateRequest) {
        if (!menuGroupRepository.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest menuCreateRequest) {
        return menuCreateRequest.getMenuProducts()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(final MenuProductCreateRequest menuProductCreateRequest) {
        Product product = productRepository.findById(menuProductCreateRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(null, null, product.getId(), menuProductCreateRequest.getQuantity(), product.getPrice());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menuDtoMapper.toMenuResponses(menus);
    }
}
