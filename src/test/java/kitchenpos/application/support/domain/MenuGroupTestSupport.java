package kitchenpos.application.support.domain;

import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;

public class MenuGroupTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static Long autoCount = 0L;

        private Long id = ++autoCount;
        private String name = "메뉴 그룹 이름" + id;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            final var result = new MenuGroup();
            result.setId(id);
            result.setName(name);
            return result;
        }

        public MenuGroupCreateRequest buildToMenuGroupCreateRequest() {
            return new MenuGroupCreateRequest(name);
        }
    }
}
