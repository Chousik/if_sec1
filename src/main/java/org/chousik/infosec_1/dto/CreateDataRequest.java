package org.chousik.infosec_1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDataRequest(
        @NotBlank
        @Size(max = 500)
        String content
) {
}
