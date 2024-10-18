package com.trilix.ai.voucher.support_tool.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TermsChunk(
        @JsonProperty("text") String text,
        @JsonProperty("metadata") TermsMetadata metadata
) {
}
