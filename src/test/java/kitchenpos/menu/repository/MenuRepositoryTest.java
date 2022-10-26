package kitchenpos.menu.repository;

import static kitchenpos.DomainFixture.뿌링클;
import static kitchenpos.DomainFixture.뿌링클_치즈볼_메뉴_생성;
import static kitchenpos.DomainFixture.세트_메뉴;
import static kitchenpos.DomainFixture.치즈볼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:truncate.sql")
class MenuRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    private Product productA;
    private Product productB;
    private Long menuGroupId;

    @BeforeEach
    void setUp() {
        productA = productRepository.save(뿌링클);
        productB = productRepository.save(치즈볼);
        menuGroupId = menuGroupRepository.save(세트_메뉴).getId();
    }

    @Test
    void 메뉴를_저장한다() {
        // given
        final var menu = menuRepository.save(뿌링클_치즈볼_메뉴_생성(menuGroupId, productA, productB));

        // when
        final var saved = menuRepository.save(menu);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(menu.getName()),
                () -> assertThat(saved.getProducts()).hasSize(2)

        );
    }

    @Test
    void ID로_메뉴를_조회한다() {
        // given
        final var saved = menuRepository.save(뿌링클_치즈볼_메뉴_생성(menuGroupId, productA, productB));

        // when
        final var found = menuRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(saved.getId());
    }
    
    @Test
    void 모든_메뉴를_조회한다() {
        // given
        menuRepository.save(뿌링클_치즈볼_메뉴_생성(menuGroupId, productA, productB));
        menuRepository.save(뿌링클_치즈볼_메뉴_생성(menuGroupId, productA, productB));

        // when
        final var found = menuRepository.findAll();

        // then
        assertThat(found).hasSize(2);
    }
}
