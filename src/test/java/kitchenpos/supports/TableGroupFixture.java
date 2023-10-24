package kitchenpos.supports;

import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    private Long id = null;

    private TableGroupFixture() {
    }

    public static TableGroupFixture fixture() {
        return new TableGroupFixture();
    }

    public TableGroupFixture id(Long id) {
        this.id = id;
        return this;
    }

    public TableGroup build() {
        return new TableGroup(id);
    }
}
