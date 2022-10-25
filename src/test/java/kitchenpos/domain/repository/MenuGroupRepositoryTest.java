package kitchenpos.domain.repository;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_저장하면_id값이_채워진다() {
        MenuGroup menuGroup = 메뉴_그룹을_생성한다("메뉴 그룹");

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        assertAll(
                () -> assertThat(savedMenuGroup).isEqualTo(menuGroup),
                () -> assertThat(savedMenuGroup.getId()).isNotNull()
        );
    }

    @Test
    void 메뉴_그룹을_id로_조회할_수_있다() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"));

        MenuGroup actual = menuGroupRepository.findById(menuGroup.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).isEqualTo(menuGroup);
    }

    @Test
    void 없는_메뉴_그룹_id로_조회하면_Optional_empty를_반환한다() {
        Optional<MenuGroup> actual = menuGroupRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_메뉴그룹을_조회할_수_있다() {
        MenuGroup menuGroup1 = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹1"));
        MenuGroup menuGroup2 = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹2"));

        List<MenuGroup> actual = menuGroupRepository.findAll();

        assertThat(actual).hasSize(2)
                .containsExactly(menuGroup1, menuGroup2);
    }

    @Test
    void 메뉴_그룹이_존재하면_true를_반환한다() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"));

        boolean actual = menuGroupRepository.existsById(menuGroup.getId());

        assertThat(actual).isTrue();
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_false를_반환한다() {
        boolean actual = menuGroupRepository.existsById(0L);

        assertThat(actual).isFalse();
    }
}
