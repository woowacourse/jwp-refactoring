package kitchenpos.support;

import static java.time.LocalDateTime.now;
import static org.assertj.core.util.Lists.emptyList;

import kitchenpos.domain.order.TableGroup;

public class TableGroupFixture {

    public static final TableGroup 빈_테이블_그룹 = new TableGroup(now(), emptyList());
}
