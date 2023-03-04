package ir.welldone.appmanager.data.repository.app;

import ir.welldone.appmanager.data.entity.App;
import ir.welldone.appmanager.view.dto.AppDTO;

import java.util.List;

public interface CustomAppRepository {

    List<App> search(AppDTO criteria, int first, int size);
    Long count(AppDTO criteria);
}
