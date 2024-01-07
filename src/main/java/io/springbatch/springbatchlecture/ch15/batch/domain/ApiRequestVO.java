package io.springbatch.springbatchlecture.ch15.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestVO {

    private long id;
    private ProductVO productVO;
    private ApiResponseVO apiResponseVO;
}
