package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.vo.Money;
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
    public MenuResponse create(MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹 아이디입니다.");
        }
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        Menu menu = new Menu(
                request.getName(),
                Money.valueOf(request.getPrice()),
                request.getMenuGroupId(),
                menuProducts
        );
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(toList());
    }
}
