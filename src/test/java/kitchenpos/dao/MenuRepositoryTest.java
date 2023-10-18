package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class MenuRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
    }

    @Test
    void 메뉴를_등록하면_ID를_부여받는다() {
        Menu 등록되기_전의_메뉴 = 새로운_메뉴("메뉴", new BigDecimal(10000), 메뉴_그룹.getId(), null);

        Menu 등록된_메뉴 = menuRepository.save(등록되기_전의_메뉴);

        assertSoftly(softly -> {
            softly.assertThat(등록된_메뉴.getId()).isNotNull();
            softly.assertThat(등록된_메뉴.getPrice()).isEqualByComparingTo(등록되기_전의_메뉴.getPrice());
            softly.assertThat(등록된_메뉴).usingRecursiveComparison()
                    .ignoringFields("id", "price")
                    .isEqualTo(등록되기_전의_메뉴);
        });
    }

    @Test
    void ID로_메뉴를_조회한다() {
        Menu 메뉴 = menuRepository.save(새로운_메뉴("메뉴", new BigDecimal(10000), 메뉴_그룹.getId(), null));

        Menu ID로_조회한_메뉴 = menuRepository.findById(메뉴.getId())
                .orElseGet(Assertions::fail);

        assertSoftly(softly -> {
            softly.assertThat(ID로_조회한_메뉴.getPrice()).isEqualByComparingTo(메뉴.getPrice());
            softly.assertThat(ID로_조회한_메뉴).usingRecursiveComparison()
                    .ignoringFields("id", "price")
                    .isEqualTo(메뉴);
        });
    }

    @Test
    void 존재하지_않는_ID로_메뉴를_조회하면_Optional_empty를_반환한다() {
        Optional<Menu> 존재하지_않는_ID로_조회한_메뉴 = menuRepository.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_ID로_조회한_메뉴).isEmpty();
    }

    @Test
    void 모든_메뉴를_조회한다() {
        Menu 메뉴1 = menuRepository.save(새로운_메뉴("메뉴1", new BigDecimal(10000), 메뉴_그룹.getId(), null));
        Menu 메뉴2 = menuRepository.save(새로운_메뉴("메뉴2", new BigDecimal(10000), 메뉴_그룹.getId(), null));

        List<Menu> 모든_메뉴 = menuRepository.findAll();

        assertThat(모든_메뉴).hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("price")
                .containsExactly(메뉴1, 메뉴2);
    }

    @Test
    void ID_목록에_있는_메뉴의_개수를_센다() {
        Menu 메뉴1 = menuRepository.save(새로운_메뉴("메뉴1", new BigDecimal(10000), 메뉴_그룹.getId(), null));
        Menu 메뉴2 = menuRepository.save(새로운_메뉴("메뉴2", new BigDecimal(10000), 메뉴_그룹.getId(), null));
        Menu 메뉴3 = menuRepository.save(새로운_메뉴("메뉴2", new BigDecimal(10000), 메뉴_그룹.getId(), null));
        Menu 메뉴4 = menuRepository.save(새로운_메뉴("메뉴2", new BigDecimal(10000), 메뉴_그룹.getId(), null));

        List<Long> ID_목록 = List.of(메뉴1.getId(), 메뉴2.getId());

        long 메뉴의_개수 = menuRepository.countByIdIn(ID_목록);

        assertThat(메뉴의_개수).isEqualTo(ID_목록.size());
    }
}
