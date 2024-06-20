package com.example.minionlinemarket.Config;

import org.hibernate.Hibernate;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class MapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Converter to handle PersistentSet and null collections
        Converter<Set<?>, Set<?>> persistentSetConverter = new Converter<Set<?>, Set<?>>() {
            @Override
            public Set<?> convert(MappingContext<Set<?>, Set<?>> context) {
                return context.getSource() == null ? new HashSet<>() : new HashSet<>(context.getSource());
            }
        };

        // Apply the converter to all mappings
        modelMapper.addConverter(persistentSetConverter);
        modelMapper.getConfiguration().setPropertyCondition(context -> Hibernate.isInitialized(context.getSource()));

        return modelMapper;
    }

    public <S, T> T convert(S source, Class<T> targetClass) {
        return this.modelMapper().map(source, targetClass);
    }
}