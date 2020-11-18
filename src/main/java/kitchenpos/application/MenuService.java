package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProductCreateInfo;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;

@Service
public class MenuService {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuService(ProductRepository productRepository, MenuGroupRepository menuGroupRepository,
        MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        Menu menu = createMenu(menuCreateRequest.getName(), menuCreateRequest.getPrice(),
            menuCreateRequest.getMenuGroupId(),
            menuCreateRequest.getMenuProductCreateInfos());

        return MenuResponse.of(menuRepository.save(menu));
    }

    private Menu createMenu(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductCreateInfo> menuProductCreateInfos) {
        BigDecimal sum = BigDecimal.ZERO;

        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }

        for (MenuProductCreateInfo menuProductCreateInfo : menuProductCreateInfos) {
            final Product product = productRepository.findById(menuProductCreateInfo.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductCreateInfo.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        return new Menu(name, price, menuGroupId,
            menuProductCreateInfos.stream().map(MenuProductCreateInfo::toMenuProduct).collect(
                Collectors.toList()));
    }

    @Transactional
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
