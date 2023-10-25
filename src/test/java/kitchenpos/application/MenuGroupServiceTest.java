package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.dto.CreateMenuGroupRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuGroupServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void create() {
        // given
        final CreateMenuGroupRequest menuGroup = new CreateMenuGroupRequest("추천메뉴");

        // when
        final MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다")
    void list() {
        // given
        final MenuGroup expect1 = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        final MenuGroup expect2 = menuGroupRepository.save(new MenuGroup("신메뉴"));

        em.flush();
        em.clear();

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(expect1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(expect2);
        });
    }
}
