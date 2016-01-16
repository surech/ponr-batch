package ch.poinzofnoreturn.batch;

import ch.poinzofnoreturn.batch.model.db.ProviderEntity;
import ch.poinzofnoreturn.batch.model.rest.PoinzProvider;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Konfiguriert die ganze Batch-Verarbeitung
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public ItemReader<PoinzProvider> reader(){
        return new ProviderReader();
    }

    @Bean
    public ItemProcessor<PoinzProvider, ProviderEntity> processor(){
        return new ProviderProcessor();
    }

    @Bean
    public ItemWriter<ProviderEntity> writer()
    {
//        return new ItemWriter<ProviderEntity>() {
//            @Override
//            public void write(List<? extends ProviderEntity> list) throws Exception {
//                for (ProviderEntity provider : list) {
//                    System.out.println("Name: " + provider.getName());
//                }
//            }
//        };
        return new ProviderWriter();
    }

    @Bean
    public JobExecutionListener listener(){
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("Los gehts!");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                System.out.println("Wir sind fertig!");
            }
        };
    }

    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1, JobExecutionListener listener) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<PoinzProvider> reader,
                      ItemWriter<ProviderEntity> writer, ItemProcessor<PoinzProvider, ProviderEntity> processor) {
        return stepBuilderFactory.get("step1")
                .<PoinzProvider, ProviderEntity> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
