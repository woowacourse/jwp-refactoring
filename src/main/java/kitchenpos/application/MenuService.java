package kitchenpos.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Product findProductById(final Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));
    }

    private Map<Product, Long> toEntities(List<CreateMenuProductRequest> menuProducts) {
        final Map<Product, Long> entities = new HashMap<>();
        for (CreateMenuProductRequest menuProduct : menuProducts) {
            entities.put(
                findProductById(menuProduct.getProductId()),
                menuProduct.getQuantity()
            );
        }

        return entities;
    }
}
