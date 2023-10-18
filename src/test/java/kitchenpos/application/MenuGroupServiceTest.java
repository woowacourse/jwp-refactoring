package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.request.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest  {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        MenuGroupCreateRequest expected = new MenuGroupCreateRequest("순살");

        // when
        MenuGroup actual = menuGroupService.create(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isPositive(),
            () -> assertThat(actual.getName()).isEqualTo("순살")
        );
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() {
        // given
        List<MenuGroup> expected = new ArrayList<>();
        expected.add(menuGroupRepository.save(new MenuGroup("순살")));
        expected.add(menuGroupRepository.save(new MenuGroup("뼈")));
        expected.add(menuGroupRepository.save(new MenuGroup("뼈맛나는순살")));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
