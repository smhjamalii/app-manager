package ir.welldone.appmanager.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * App entity stores application information
 * @author Hossein Jamali
 */
@Entity
@Table(schema = "APP_MANAGER", name = "APP")
@SequenceGenerator(allocationSize = 5, initialValue = 100, name = "APP_SEQ")
@Data @AllArgsConstructor
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_SEQ")
    private Long id;
    private String name;
    private String desc;
    private String domain;
    private Boolean enabled;
    @Version
    private int version;

    public App() {
        this.enabled = Boolean.TRUE;
    }
}
