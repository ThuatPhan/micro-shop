package org.example.cartservice.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    boolean hasNext;
    List<T> data;

    public static <T> PageResponse<T> of(boolean hasNext, List<T> data) {
        return PageResponse.<T>builder().hasNext(hasNext).data(data).build();
    }
}
