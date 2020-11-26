package kitchenpos.domain.menu.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ProductRepositoryTest {
    private static final String 상품_이름_후라이드치킨 = "후라이드 치킨";
    private static final String 상품_이름_코카콜라 = "코카콜라";
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.00");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.00");
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    private Product product1;
    private Product product2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        product1 = new Product(상품_이름_후라이드치킨, 상품_가격_15000원);
        product2 = new Product(상품_이름_코카콜라, 상품_가격_1000원);
        productRepository.save(product1);
        productRepository.save(product2);

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트));
        menu = menuRepository.save(new Menu(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup));
    }

    @DisplayName("Product의 목록 조회를 요청할 경우, 올바르게 수행된다.")
    @Test
    void findAllTest() {
        menuProductRepository.save(new MenuProduct(menu, product1, 1L));

        List<Product> products = productRepository.findAllByMenu(menu);

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getId()).isEqualTo(product1.getId());
        assertThat(products.get(0).getName()).isEqualTo(product1.getName());
        assertThat(products.get(0).getPrice()).isEqualTo(product1.getPrice());
    }
}
