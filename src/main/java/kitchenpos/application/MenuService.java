package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.menu.CreateMenuProductRequest;
import kitchenpos.dto.request.menu.CreateMenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        MenuRepository menuRepository,
        MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {
        return menuRepository.save(new Menu(
            request.getName(),
            request.getPrice(),
            findMenuGroupById(request.getMenuGroupId()),
            toEntities(request.getMenuProducts())
        ));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private MenuGroup findMenuGroupById(final Long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }

    private Map<Product, Long> toEntities(List<CreateMenuProductRequest> menuProducts) {
        final List<Product> products = findProductsByIds(menuProducts);

        final Map<Long, Long> menuProductsMap = menuProducts.stream()
            .collect(Collectors.toMap(
                it -> it.getProductId(),
                it -> it.getQuantity()
            ));

        return products.stream()
            .collect(Collectors.toMap(
                it -> it,
                it -> menuProductsMap.get(it.getId())
            ));
    }

    private List<Product> findProductsByIds(List<CreateMenuProductRequest> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
            .map(it -> it.getProductId())
            .collect(Collectors.toList());

        final List<Product> products = productRepository.findAllByIdIn(productIds);

        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("존재하지 않는 제품입니다.");
        }

        return products;
    }
}
