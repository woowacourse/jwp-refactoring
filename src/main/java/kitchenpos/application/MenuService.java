package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.repository.MenuRepository;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupDao menuGroupDao, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productRepository = productRepository;
    }

    public MenuResponse create(MenuRequest request) {
        Menu menu = request.toMenu();
        validateIsExistMenuGroup(menu);
        validateProductAndPrice(menu);

        return new MenuResponse(menuRepository.save(menu));
    }

    private void validateIsExistMenuGroup(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validateProductAndPrice(Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        menu.validatePriceIsCheaperThanSum(sum);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
