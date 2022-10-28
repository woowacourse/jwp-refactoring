package kitchenpos.order.repository;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static kitchenpos.support.DomainFixture.뿌링클;
import static kitchenpos.support.DomainFixture.세트_메뉴;
import static kitchenpos.support.DomainFixture.치즈볼;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:truncate.sql")
class OrderTableRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableRepository tableRepository;

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
    void 테이블을_저장한다() {
        // given
        final var table = 빈_테이블_생성();

        // when
        final var saved = tableRepository.save(table);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getGuestNumber()).isEqualTo(table.getGuestNumber())
        );
    }

    @Test
    void ID로_테이블을_조회한다() {
        // given
        final var saved = tableRepository.save(빈_테이블_생성());

        // when
        final var found = tableRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(saved.getId());
    }
    
    @Test
    void 모든_테이블을_조회한다() {
        // given
        tableRepository.save(빈_테이블_생성());
        tableRepository.save(빈_테이블_생성());
        
        // when
        final var founds = tableRepository.findAll();

        // then
        assertThat(founds).hasSize(2);
    }
}
