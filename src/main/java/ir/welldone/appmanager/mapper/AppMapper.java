package ir.welldone.appmanager.mapper;

import ir.welldone.appmanager.data.entity.App;
import ir.welldone.appmanager.view.dto.AppDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppMapper {

    public static AppDTO entityToTransferObject(App app){
        log.debug("Mapping App entity to AppDTO");
        AppDTO appDTO = AppDTO.builder()
                .id(app.getId())
                .name(app.getName())
                .desc(app.getDesc())
                .domain(app.getDomain())
                .enabled(app.getEnabled())
                .version(app.getVersion())
                .build();

        return appDTO;
    }

    public static App transferObjectToEntity(AppDTO appDTO){
        log.debug("Mapping AppDTO to App entity");
        App app = new App(appDTO.getId(),
                appDTO.getName(),
                appDTO.getDesc(),
                appDTO.getDomain(),
                appDTO.getEnabled(),
                appDTO.getVersion());

        return app;
    }

}
