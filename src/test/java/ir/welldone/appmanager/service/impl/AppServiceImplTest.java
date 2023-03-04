package ir.welldone.appmanager.service.impl;

import ir.welldone.appmanager.service.interfaces.app.AppService;
import ir.welldone.appmanager.view.dto.AppDTO;
import ir.welldone.appmanager.view.dto.PagedResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest
public class AppServiceImplTest {

    @Autowired
    private AppService appService;

    @Test
    public void shouldSaveANewEntity(){
        AppDTO appDTO = AppDTO.builder()
                .name("Chita Healthcare Manager")
                .desc("Monitors all Chitas living on the earth")
                .domain("https://chita.gov.ir")
                .enabled(Boolean.TRUE)
                .build();

        appService.save(appDTO);

        PagedResult<AppDTO> searchResult = appService.search(appDTO, 0, 10);
        Assert.notEmpty(searchResult.getResult(), "Search result for newly created entity must not be empty");
    }

    @Test
    public void shouldFindARecordById(){
        Optional<AppDTO> appDTO = appService.findById(1L);
        Assert.isTrue(appDTO.isPresent(), "FindById should find corresponding record with the specified id");
    }

    @Test
    public void shouldFindRecordsBySearch(){
        AppDTO appDTO = AppDTO.builder()
                .name("Birds")
                .enabled(Boolean.TRUE)
                .build();

        PagedResult<AppDTO> searchResult = appService.search(appDTO, 0, 10);
        Assert.notEmpty(searchResult.getResult(), "Search result should not be empty");
        Assert.notNull(searchResult.getTotalRecords(), "Search result total count should not be null");
        Assert.isTrue(searchResult.getTotalRecords().equals(3L), "Search result for active apps should contain 3 records");
    }

    @Test
    public void shouldUpdateARecord(){
        Optional<AppDTO> appDTO = appService.findById(2L);
        appDTO.ifPresent(app -> {
            app.setName("Gymnastic");
            appService.update(app);
        });
        Optional<AppDTO> updatedAppDto = appService.findById(2L);
        Assert.isTrue(updatedAppDto.get().getName().equals("Gymnastic"), "Updated record should have a new name");
    }

    @Test
    public void shouldDeleteAnApp(){
        Optional<AppDTO> appDTO = appService.findById(4L);
        appDTO.ifPresent(app -> appService.delete(app.getId()));
        Optional<AppDTO> deletedApp = appService.findById(4L);
        Assert.isTrue(deletedApp.isEmpty(), "Deleted app must be deleted");
    }
}
