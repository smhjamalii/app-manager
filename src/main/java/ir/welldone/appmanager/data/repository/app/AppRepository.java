package ir.welldone.appmanager.data.repository.app;

import ir.welldone.appmanager.data.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<App, Long>, CustomAppRepository {
}
