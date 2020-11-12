package kitchenpos.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@DataJpaTest
public class MenuProductRepositoryTest {
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final Long 메뉴_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final String 상품_후라이드_치킨 = "후라이드 치킨";
    private static final String 상품_코카콜라 = "코카콜라";
    private static final Long 상품_1개 = 1L;
    private static final Long 상품_2개 = 2L;
    private static final Long 상품_ID_1 = 1L;
    private static final Long 상품_ID_2 = 2L;
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.0");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.0");
    private static final Long 메뉴_상품_SEQ_1 = 1L;
    private static final Long 메뉴_상품_SEQ_2 = 2L;

    @Autowired
    private MenuProductRepository menuProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private Menu menu;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        menu = new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);
        product1 = new Product(상품_ID_1, 상품_후라이드_치킨, 상품_가격_15000원);
        product2 = new Product(상품_ID_2, 상품_코카콜라, 상품_가격_1000원);
        menuGroupRepository.save(menuGroup);
        menuRepository.save(menu);
        productRepository.save(product1);
        productRepository.save(product2);
    }

    @DisplayName("MenuProduct를 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        MenuProduct menuProduct = new MenuProduct(메뉴_상품_SEQ_1, menu, product1, 상품_1개);

        MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
        Long count = menuProductRepository.count();

        assertThat(count).isEqualTo(1L);
        assertThat(savedMenuProduct.getSeq()).isEqualTo(메뉴_상품_SEQ_1);
        assertThat(savedMenuProduct.getQuantity()).isEqualTo(상품_1개);
        assertThat(savedMenuProduct.getMenu().getId()).isEqualTo(메뉴_ID_1);
        assertThat(savedMenuProduct.getProduct().getId()).isEqualTo(상품_ID_1);
    }

    @DisplayName("MenuProduct를 Menu를 통해 필터링하여 조회할 경우, 올바르게 수행된다.")
    @Test
    void findAllByMenuTest() {
        MenuProduct menuProduct1 = new MenuProduct(메뉴_상품_SEQ_1, menu, product1, 상품_1개);
        MenuProduct menuProduct2 = new MenuProduct(메뉴_상품_SEQ_2, menu, product2, 상품_2개);
        menuProductRepository.save(menuProduct1);
        menuProductRepository.save(menuProduct2);

        List<MenuProduct> foundedMenuProducts = menuProductRepository.findAllByMenu(menu);

        assertThat(foundedMenuProducts).
                hasSize(2).
                extracting("seq").
                containsOnly(메뉴_상품_SEQ_1, 메뉴_상품_SEQ_2);
    }
}
