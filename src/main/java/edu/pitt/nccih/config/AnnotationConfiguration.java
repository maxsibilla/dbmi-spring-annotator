package edu.pitt.nccih.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:annotations.properties")
public class AnnotationConfiguration {

    @Value("${annotation.add}")
    private String addAnnotation;

    @Value("${annotation.file}")
    private String annotationFile;

    @Value("${annotation.type}")
    private String annotationType;

    @Value("${annotation.media}")
    private String annotationMedia;

    @Value("${annotation.subtitle}")
    private String annotationSubtitle;

    @Value("${annotation.serialize}")
    private String annotationSerialize;

    @Value("${annotation.preAnnotate}")
    private String annotationPreAnnotate;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
