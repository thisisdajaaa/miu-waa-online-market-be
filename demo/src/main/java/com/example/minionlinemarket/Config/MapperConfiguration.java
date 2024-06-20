package com.example.minionlinemarket.config;

import com.example.minionlinemarket.model.Dto.Response.ProductDetailDto;
import com.example.minionlinemarket.model.Product;
import org.hibernate.Hibernate;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class MapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        Converter<Set<?>, Set<?>> persistentSetConverter = context -> context.getSource() == null ? new HashSet<>() : new HashSet<>(context.getSource());

        mapper.addConverter(persistentSetConverter);
        mapper.getConfiguration().setPropertyCondition(context -> Hibernate.isInitialized(context.getSource()));

        mapper.typeMap(Product.class, ProductDetailDto.class).addMappings(item ->
                item.using((Converter<byte[], String>) context ->
                        context.getSource() == null ? null : Base64.getEncoder().encodeToString(context.getSource())
                ).map(Product::getImage, ProductDetailDto::setBase64Image)
        );

        // Add converter for Base64 decoding
        mapper.typeMap(ProductDetailDto.class, Product.class).addMappings(item ->
                item.using((Converter<String, byte[]>) context ->
                        context.getSource() == null ? null : Base64.getDecoder().decode(context.getSource())
                ).map(ProductDetailDto::getBase64Image, Product::setImage)
        );

        return mapper;
    }

    public <S, T> T convert(S source, Class<T> targetClass) {
        return this.modelMapper().map(source, targetClass);
    }
}
