package kitchenpos.application;

import kitchenpos.menugroup.controller.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@Sql("/truncate.sql")
class MenuGroupServiceTest {
    
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    
    @Autowired
    private MenuGroupService menuGroupService;
    
    @Test
    void 메뉴그룹을_생성한다() {
        //given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("중국식 메뉴 그룹");
        
        //when
        MenuGroup expected = MENU_GROUP("중국식 메뉴 그룹");
        MenuGroup actual = menuGroupService.create(request);
        
        //then
        assertThat(actual.getName())
                .isEqualTo(expected.getName());
    }
    
    @Test
    void 메뉴_그룹_목록을_조회한다() {
        //given
        MenuGroup menuGroup1 = MENU_GROUP("중국식 메뉴 그룹");
        MenuGroup savedMenuGroup1 = menuGroupRepository.save(menuGroup1);
        MenuGroup menuGroup2 = MENU_GROUP("한식 메뉴 그룹");
        MenuGroup savedMenuGroup2 = menuGroupRepository.save(menuGroup2);
        
        //when
        List<MenuGroup> queriedMenuGroups = menuGroupService.list();
        
        //then
        List<MenuGroup> expected = List.of(savedMenuGroup1, savedMenuGroup2);
        assertThat(queriedMenuGroups)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
