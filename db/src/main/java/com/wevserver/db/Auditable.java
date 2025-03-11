package com.wevserver.db;

public interface Auditable {

    void setCreatedAt(final Long createdAt);

    Long getCreatedAt();

    void setUpdatedAt(final Long updatedAt);

    Long getUpdatedAt();
}
