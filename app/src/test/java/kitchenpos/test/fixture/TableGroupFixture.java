package kitchenpos.test.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.TableMapper;
import kitchenpos.application.TableValidator;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup 테이블_그룹(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

    public static TableGroup 테이블_그룹(
            LocalDateTime createdDate,
            List<Long> tableIds,
            TableMapper tableMapper,
            TableValidator tableValidator
    ) {
        return new TableGroup(
                createdDate,
                tableIds,
                tableMapper,
                tableValidator
        );
    }
}
