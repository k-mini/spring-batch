package io.springbatch.springbatchlecture.ch15.batch.chunk.writer;
import io.springbatch.springbatchlecture.ch15.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.ch15.batch.domain.ApiResponseVO;
import io.springbatch.springbatchlecture.ch15.service.AbstractApiService;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

public class ApiItemWriter3 extends FlatFileItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;

    public ApiItemWriter3(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO responseVO = apiService.service(items);
        System.out.println("responseVO = " + responseVO);

        items.forEach(item -> item.setApiResponseVO(responseVO));

        super.write(items);
    }
}
