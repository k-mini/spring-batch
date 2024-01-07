package io.springbatch.springbatchlecture.ch15.batch.chunk.processor;

import io.springbatch.springbatchlecture.ch15.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.ch15.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor3 implements ItemProcessor<ProductVO, ApiRequestVO> {

    @Override
    public ApiRequestVO process(ProductVO item) throws Exception {
        return ApiRequestVO.builder()
                .id(item.getId())
                .productVO(item)
                .build();
    }
}
