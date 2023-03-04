package ir.welldone.appmanager.data.repository.app;

import ir.welldone.appmanager.data.entity.App;
import ir.welldone.appmanager.view.dto.AppDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class AppRepositoryTest {

    @Autowired
    private AppRepository appRepository;

    @Test
    public void shouldFindAppropriateResults(){
        AppDTO appDTO = AppDTO.builder()
                .name("Gym")
                .build();
        List<App> result = appRepository.search(appDTO, 0, 10);
        Assert.notNull(result, "Result should not be null");
        Assert.notEmpty(result, "Must have at least one element");
    }

}
