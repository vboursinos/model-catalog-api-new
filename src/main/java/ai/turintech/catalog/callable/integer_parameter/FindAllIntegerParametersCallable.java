package ai.turintech.catalog.callable.integer_parameter;

import ai.turintech.catalog.repository.IntegerParameterRepository;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterMapper;
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
public class FindAllIntegerParametersCallable implements Callable<List<IntegerParameterDTO>> {

    // Constructor injection is the recommended way for mandatory dependencies
    public FindAllIntegerParametersCallable() {
    }

    @Autowired
    private IntegerParameterRepository integerParameterRepository;

    @Autowired
    private IntegerParameterMapper integerParameterMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    @Override
    public List<IntegerParameterDTO> call() throws Exception {
        return integerParameterRepository
                .findAll()
                .stream()
                .map(integerParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}