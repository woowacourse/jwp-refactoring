package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_등록하면_ID를_부여받는다() {
        MenuGroup 등록되기_전의_메뉴_그룹 = 새로운_메뉴_그룹("메뉴 그룹");

        MenuGroup 등록된_메뉴_그룹 = menuGroupRepository.save(등록되기_전의_메뉴_그룹);

        assertSoftly(softly -> {
            softly.assertThat(등록된_메뉴_그룹.getId()).isNotNull();
            softly.assertThat(등록된_메뉴_그룹).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(등록되기_전의_메뉴_그룹);
        });
    }

    @Test
    void ID로_메뉴_그룹을_조회한다() {
        MenuGroup 메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));

        MenuGroup ID로_조회한_메뉴_그룹 = menuGroupRepository.findById(메뉴_그룹.getId())
                .orElseGet(Assertions::fail);

        assertThat(ID로_조회한_메뉴_그룹).usingRecursiveComparison()
                .isEqualTo(메뉴_그룹);
    }

    @Test
    void 존재하지_않는_ID로_메뉴_그룹을_조회하면_Optional_empty를_반환한다() {
        Optional<MenuGroup> 존재하지_않는_ID로_조회한_메뉴_그룹 = menuGroupRepository.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_ID로_조회한_메뉴_그룹).isEmpty();
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() {
        MenuGroup 메뉴_그룹1 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹1"));
        MenuGroup 메뉴_그룹2 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹2"));

        List<MenuGroup> 모든_메뉴_그룹 = menuGroupRepository.findAll();

        assertThat(모든_메뉴_그룹).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(메뉴_그룹1, 메뉴_그룹2);
    }

    @Test
    void 메뉴_그룹의_존재_여부를_반환한다() {
        MenuGroup 등록된_메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
        MenuGroup 등록되지_않은_메뉴_그룹 = 새로운_메뉴_그룹("메뉴 그룹");

        assertSoftly(softly -> {
            softly.assertThat(menuGroupRepository.existsById(등록된_메뉴_그룹.getId())).isTrue();
            softly.assertThat(menuGroupRepository.existsById(등록되지_않은_메뉴_그룹.getId())).isFalse();
        });
    }
}
