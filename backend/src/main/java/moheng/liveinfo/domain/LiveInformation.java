package moheng.liveinfo.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;

@Table(name = "live_information")
@Entity
public class LiveInformation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}