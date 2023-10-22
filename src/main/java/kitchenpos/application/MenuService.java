package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup findMenuGroup = menuGroupRepository.findMenuGroupById(request.getMenuGroupId());
        final Menu newMenu = Menu.withEmptyMenuProducts(
                new Name(request.getName()),
                new Price(request.getPrice()),
                findMenuGroup
        );
        newMenu.addMenuProducts(createMenuProducts(request));
        final Menu savedMenu = menuRepository.save(newMenu);

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductCreateRequest -> {
                    final Product findProduct = productRepository.findProductById(menuProductCreateRequest.getProductId());
                    return MenuProduct.withoutMenu(findProduct, new Quantity(menuProductCreateRequest.getQuantity()));
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.joinMenuGroupAll());
    }
}
