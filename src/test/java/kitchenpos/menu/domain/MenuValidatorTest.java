package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class MenuValidatorTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹과 상품이 존재하고 메뉴 가격이 메뉴를 구성하는 상품의 가격 총합보다 작으면 예외가 발생하지 않는다")
    void create() {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

        em.flush();
        em.clear();

        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드.getId(), 2L);
        final Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatCode(() -> menuValidator.validate(menu))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("메뉴를 등록할 때 지정한 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    void create_nullMenuGroup() {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));

        em.flush();
        em.clear();

        final Long invalidMenuGroupId = -999L;
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드.getId(), 2L);
        final Menu invalidMenu = new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), invalidMenuGroupId, List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("메뉴를 등록할 때 상품이 유효한 상품이 아니라면 예외가 발생한다.")
    void create_invalidProduct() {
        // given
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

        em.flush();
        em.clear();

        final Long invalidProductId = -999L;
        final MenuProduct invalidMenuProductRequest = new MenuProduct(invalidProductId, 2L);
        final Menu invalidMenu = new Menu("후라이드+후라이드", BigDecimal.valueOf(30000), 두마리메뉴.getId(), List.of(invalidMenuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("메뉴를 등록할 때 상품의 가격 합이 메뉴의 가격보다 작으면 예외가 발생한다.")
    void create_invalidPrice_moreThanSum() {
        // given
        final Product 후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        final MenuGroup 두마리메뉴 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

        em.flush();
        em.clear();

        final BigDecimal invalidPrice = BigDecimal.valueOf(50000);
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드.getId(), 2L);
        final Menu invalidMenu = new Menu("후라이드+후라이드", invalidPrice, 두마리메뉴.getId(), List.of(후라이드_2개));

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 메뉴를 구성하는 상품의 가격 합보다 작아야 합니다.");
    }
}
