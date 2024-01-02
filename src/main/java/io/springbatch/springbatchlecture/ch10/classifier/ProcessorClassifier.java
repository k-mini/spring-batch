package io.springbatch.springbatchlecture.ch10.classifier;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C extends ProcessorInfo, T extends ItemProcessor<?,?>>
        implements Classifier<C,T> {

    private Map<Integer, ItemProcessor<? super ProcessorInfo,? extends ProcessorInfo>> processorMap = new HashMap<>();

    @Override
    public T classify(C classifiable) {
        return (T) processorMap.get( ((ProcessorInfo) classifiable).getId()  );
    }

    public void setProcessorMap(Map<Integer, ItemProcessor<? super ProcessorInfo,? extends ProcessorInfo>> processorMap) {
        this.processorMap = processorMap;
    }
}
