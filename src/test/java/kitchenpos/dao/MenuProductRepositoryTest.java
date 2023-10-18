package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_상품;
import static kitchenpos.support.TestFixtureFactory.새로운_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class MenuProductRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    private MenuGroup 메뉴_그룹;
    private Menu 메뉴;
    private Product 상품;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
        메뉴 = menuRepository.save(새로운_메뉴("메뉴", new BigDecimal(10000), 메뉴_그룹.getId(), null));
        상품 = productRepository.save(새로운_상품("상품", new BigDecimal(1000)));
    }

    @Test
    void 메뉴_상품을_등록하면_Seq를_부여받는다() {
        MenuProduct 등록되지_않은_메뉴_상품 = 새로운_메뉴_상품(메뉴.getId(), 상품.getId(), 10);

        MenuProduct 등록된_메뉴_상품 = menuProductRepository.save(등록되지_않은_메뉴_상품);

        assertSoftly(softly -> {
            softly.assertThat(등록된_메뉴_상품.getSeq()).isNotNull();
            softly.assertThat(등록된_메뉴_상품).usingRecursiveComparison()
                    .ignoringFields("seq")
                    .isEqualTo(등록되지_않은_메뉴_상품);
        });
    }

    @Test
    void Seq로_메뉴_상품을_조회한다() {
        MenuProduct 메뉴_상품 = menuProductRepository.save(새로운_메뉴_상품(메뉴.getId(), 상품.getId(), 10));

        MenuProduct Seq로_조회한_메뉴_상품 = menuProductRepository.findById(메뉴_상품.getSeq())
                .orElseGet(Assertions::fail);

        assertThat(Seq로_조회한_메뉴_상품).usingRecursiveComparison()
                .isEqualTo(메뉴_상품);
    }

    @Test
    void 존재하지_않는_Seq로_메뉴_상품을_조회하면_Optional_empty를_반환한다() {
        Optional<MenuProduct> 존재하지_않는_Seq로_조회한_메뉴_상품 = menuProductRepository.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_Seq로_조회한_메뉴_상품).isEmpty();
    }

    @Test
    void 모든_메뉴_상품을_조회힌다() {
        MenuProduct 메뉴_상품1 = menuProductRepository.save(새로운_메뉴_상품(메뉴.getId(), 상품.getId(), 4));
        MenuProduct 메뉴_상품2 = menuProductRepository.save(새로운_메뉴_상품(메뉴.getId(), 상품.getId(), 6));

        List<MenuProduct> 모든_메뉴_상품 = menuProductRepository.findAll();

        assertThat(모든_메뉴_상품).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(메뉴_상품1, 메뉴_상품2);
    }

    @Test
    void 메뉴_ID로_메뉴의_모든_메뉴_상품을_조회한다() {
        Menu 또다른_메뉴 = menuRepository.save(새로운_메뉴("메뉴2", new BigDecimal(0), 메뉴_그룹.getId(), null));

        MenuProduct 원래_메뉴의_메뉴_상품1 = menuProductRepository.save(새로운_메뉴_상품(메뉴.getId(), 상품.getId(), 4));
        MenuProduct 원래_메뉴의_메뉴_상품2 = menuProductRepository.save(새로운_메뉴_상품(메뉴.getId(), 상품.getId(), 6));
        MenuProduct 또다른_메뉴의_메뉴_상품 = menuProductRepository.save(새로운_메뉴_상품(또다른_메뉴.getId(), 상품.getId(), 1));

        List<MenuProduct> 또다른_메뉴의_모든_메뉴_상품 = menuProductRepository.findAllByMenuId(또다른_메뉴.getId());

        assertThat(또다른_메뉴의_모든_메뉴_상품).hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(또다른_메뉴의_메뉴_상품);
    }
}
