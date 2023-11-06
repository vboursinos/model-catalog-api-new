package ai.turintech.catalog.callable.categorical_parameter;

import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterMapper;
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
public class FindAllCategoricalParametersCallable implements Callable<List<CategoricalParameterDTO>> {

    // Constructor injection is the recommended way for mandatory dependencies
    public FindAllCategoricalParametersCallable() {
    }

    @Autowired
    private CategoricalParameterRepository categoricalParameterRepository;

    @Autowired
    private CategoricalParameterMapper categoricalParameterMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    @Override
    public List<CategoricalParameterDTO> call() throws Exception {
        return categoricalParameterRepository
                .findAll()
                .stream()
                .map(categoricalParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}