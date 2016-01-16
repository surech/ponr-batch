package ch.poinzofnoreturn.batch;

import ch.poinzofnoreturn.batch.model.db.ProviderEntity;
import ch.poinzofnoreturn.batch.model.rest.PoinzProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by surech on 10.01.16.
 */
public class ProviderProcessor implements ItemProcessor<PoinzProvider, ProviderEntity> {

    /**
     * Factory zum Erstellen von geografischen Punkten
     */
    private final static GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    @Override
    public ProviderEntity process(PoinzProvider poinzProvider) throws Exception {
        ProviderEntity result = new ProviderEntity();
        result.setName(bt(poinzProvider.getName(), 255));
        result.setStreet(poinzProvider.getStreet());
        result.setZip(poinzProvider.getZip());
        result.setCity(poinzProvider.getCity());
        result.setDescription(bt(poinzProvider.getDescription(), 512));
        result.setPoinzId(poinzProvider.getId());
        result.setLocation(createPoint(poinzProvider.getLatitude(), poinzProvider.getLongitude()));

        return result;
    }

    public String bt(String value, int lenght) {
        return StringUtils.substring(value, 0, lenght);
    }

    public Point createPoint(String latitude, String longitude) {
        // Zahlen paren
        double x = Double.parseDouble(latitude);
        double y = Double.parseDouble(longitude);

        // Koordinaten erstellen
        Coordinate coordinate = new Coordinate(x, y);
        return GEOMETRY_FACTORY.createPoint(coordinate);
    }
}
