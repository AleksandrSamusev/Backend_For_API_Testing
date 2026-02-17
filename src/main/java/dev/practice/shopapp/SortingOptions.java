package dev.practice.shopapp;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Available strategies for sorting user result lists",
        example = "ID_ASC"
)
public enum SortingOptions {
    @Schema(description = "Sort by first name in ascending order")
    FIRST_NAME_ASC,
    @Schema(description = "Sort by first name in descending order")
    FIRST_NAME_DESC,
    @Schema(description = "Sort by last name in ascending order")
    LAST_NAME_ASC,
    @Schema(description = "Sort by last name in descending order")
    LAST_NAME_DESC,
    @Schema(description = "Sort by ID in ascending order")
    ID_ASC,
    @Schema(description = "Sort by ID in descending order")
    ID_DESC;

}
