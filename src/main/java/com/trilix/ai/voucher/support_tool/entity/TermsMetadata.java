package com.trilix.ai.voucher.support_tool.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TermsMetadata(
        @JsonProperty("section") String section,
        @JsonProperty("subsection") String subsection
) {
}
