package ai.turintech.catalog.callable.parameter;

import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ParameterMapper;
import ai.turintech.catalog.utils.PaginationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Transactional
@Component
@Scope("prototype")
public class FindAllParametersCallable implements Callable<List<ParameterDTO>> {

    // Constructor injection is the recommended way for mandatory dependencies
    public FindAllParametersCallable() {
    }

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    @Override
    public List<ParameterDTO> call() throws Exception {
        return parameterRepository
                .findAll()
                .stream()
                .map(parameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}