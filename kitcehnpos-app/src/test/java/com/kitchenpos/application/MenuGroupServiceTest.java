package com.kitchenpos.application;

import com.kitchenpos.application.dto.MenuGroupCreateRequest;
import com.kitchenpos.domain.MenuGroup;
import com.kitchenpos.fixture.MenuGroupFixture;
import com.kitchenpos.helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends IntegrationTestHelper {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹_생성();

        // when
        MenuGroup result = menuGroupService.create(new MenuGroupCreateRequest(menuGroup.getName()));

        // then
        assertThat(menuGroup.getName()).isEqualTo(result.getName());
    }

    @Test
    void 모두_조회한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(new MenuGroupCreateRequest(MenuGroupFixture.메뉴_그룹_생성().getName()));

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(menuGroup);
        });
    }
}
