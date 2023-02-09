package org.unicrm.analytic.dto;

import lombok.Builder;
import lombok.Data;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;

import java.util.UUID;

@Data
public class CurrentInformation {
    UUID userId;
    Long departmentId;
    Status status;
    TimeInterval timeInterval;
    int page;
    int countElements;
}
