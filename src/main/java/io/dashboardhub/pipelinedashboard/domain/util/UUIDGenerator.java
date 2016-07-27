package io.dashboardhub.pipelinedashboard.domain.util;

import java.util.UUID;

public class UUIDGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
