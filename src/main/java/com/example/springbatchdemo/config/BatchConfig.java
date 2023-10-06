package com.example.springbatchdemo.config;

import com.example.springbatchdemo.entity.Product;
import com.example.springbatchdemo.listener.MyJobListener;
import com.example.springbatchdemo.processor.ProductProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<Product> reader(){
        FlatFileItemReader<Product>reader=new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("products.csv"));
        reader.setLineMapper(new DefaultLineMapper(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setDelimiter(DELIMITER_COMMA);
                setNames("productId","productCode","productCost");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper(){{
                setTargetType(Product.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<Product,Product> processor(){
        return new ProductProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Product>writer(){
        JdbcBatchItemWriter<Product>writer=new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO PRODUCTSABC(PID,PCODE,PCOST,PDISC,PGST) VALUES(:productId,:productCode,:productCost,:productDisc,:productGst)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }

    @Bean
    public JobExecutionListener listener(){
        return new MyJobListener();
    }

    @Bean
    public Step stepA(){
        return stepBuilderFactory.get("stepA")
                .<Product,Product>chunk(3)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job jobA(){
         return jobBuilderFactory.get("jobA")
                 .incrementer(new RunIdIncrementer())
                 .listener(listener())
                 .start(stepA())
                 .build();
    }
}
