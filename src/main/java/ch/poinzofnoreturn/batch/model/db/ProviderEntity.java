package ch.poinzofnoreturn.batch.model.db;

import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert einen Anbieter
 */
@Entity
@Table(name = "provider")
public class ProviderEntity extends CreateTimeTrackEntity {

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Geografische Position der Anlage
     */
    @Column(name = "location", nullable = false)
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Point location;

    @Column(name = "street")
    private String street;

    @Column(name = "zip")
    private String zip;

    @Column(name = "city")
    private String city;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "provider")
    private List<PointcodeEntity> pointcodes;

    @Column(name = "poinzId")
    private String poinzId;

    public ProviderEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPoinzId() {
        return poinzId;
    }

    public void setPoinzId(String poinzId) {
        this.poinzId = poinzId;
    }

    public List<PointcodeEntity> getPointcodes() {
        if (pointcodes == null) {
            pointcodes = new ArrayList<>();
        }
        return pointcodes;
    }
}
