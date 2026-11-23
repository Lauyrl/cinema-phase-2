package vn.edu.uet.cinema.api;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import vn.edu.uet.cinema.business.PageResult;

@Schema(name = "PageResponse")
public record PageResponse<T>(
        @Schema(description = "Items on the current page.") List<T> items,
        @Schema(description = "Current 1-based page number.", example = "1") int page,
        @Schema(description = "Number of items per page.", example = "20") int size,
        @Schema(description = "Total number of matching items.", example = "42") long totalItems,
        @Schema(description = "Total number of pages.", example = "3") int totalPages) {

    public static <T> PageResponse<T> from(PageResult<T> result) {
        return new PageResponse<>(
                result.items(),
                result.page(),
                result.size(),
                result.totalItems(),
                result.totalPages());
    }
}