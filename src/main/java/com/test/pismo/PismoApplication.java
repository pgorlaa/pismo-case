package com.test.pismo;

import com.test.pismo.repository.OperationTypeRepository;
import com.test.pismo.model.OperationTypeEnum;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class PismoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PismoApplication.class, args);
    }

    @Bean
    public CommandLineRunner insertDefaultOperationTypes(OperationTypeRepository operationTypeRepository) {

        return (args) -> {
            operationTypeRepository.saveAll(
                    Arrays.stream(OperationTypeEnum.values()).map(OperationTypeEnum::getOperation).toList()
            );
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
