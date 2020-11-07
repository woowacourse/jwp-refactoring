package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.dto.TableGroupResponse;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 생성")
    @Test
    void create() {
        TableGroupResponse response = tableGroupService.create(TABLE_GROUP_CREATE_REQUEST);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void ungroup() {
    }
}