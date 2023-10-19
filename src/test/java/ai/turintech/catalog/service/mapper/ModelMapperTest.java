package ai.turintech.catalog.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ModelMapperTest {

    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        modelMapper = new ModelMapperImpl();
    }
}
