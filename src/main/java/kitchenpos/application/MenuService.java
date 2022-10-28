package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuCreateResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import kitchenpos.ui.mapper.MenuDtoMapper;
import kitchenpos.ui.mapper.MenuMapper;
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
    public MenuCreateResponse create(final MenuCreateRequest menuCreateRequest) {
        if (!menuGroupRepository.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProduct> menuProducts = menuCreateRequest.getMenuProducts()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());

        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getPrice().multiply(menuProduct.getQuantity()));
        }

        Menu menu = menuMapper.toMenu(menuCreateRequest, menuProducts);

        if (menu.getPrice().getValue().compareTo(sum.getValue()) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        return menuDtoMapper.toMenuCreateResponse(savedMenu);
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
