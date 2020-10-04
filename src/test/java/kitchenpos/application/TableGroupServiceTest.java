package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends KitchenPosServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void create() {
        TableGroup tableGroup = new TableGroup();

//        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
    }

    @Test
    void ungroup() {
    }
}
