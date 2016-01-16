package ch.poinzofnoreturn.batch;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

/**
 * Created by surech on 16.01.16.
 */
public class ProviderReaderTest {

    /** Factory zum Erstellen von geografischen Punkten */
    private final static GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private ProviderReader reader = new ProviderReader(true);

    @Test
    public void testMoveX() throws Exception {
        // Testdaten erfassen
        Coordinate startCoordinate = new Coordinate(47, 7.5);
        Point start = GEOMETRY_FACTORY.createPoint(startCoordinate);

        // Test ausführen
        Point result = reader.move(start, 1000, 0);

        // Überprüfen
        Assert.assertEquals(47.009000009, result.getX(), 0.00000001);
        Assert.assertEquals(7.5, result.getY(), 0);

    }

    @Test
    public void testMoveY() throws Exception {
        // Testdaten erfassen
        Coordinate startCoordinate = new Coordinate(47, 7.5);
        Point start = GEOMETRY_FACTORY.createPoint(startCoordinate);

        // Test ausführen
        Point result = reader.move(start, 0, 1000);

        // Überprüfen
        Assert.assertEquals(47, result.getX(), 0);
        Assert.assertEquals(7.4909304774, result.getY(), 0.00000001);
    }

    @Test
    public void testBuildScanGrid() throws Exception {
        // Test ausführen
        Stack<Point> result = reader.buildScanGrid();

        System.out.println("Länge: " + result.size());
        StringBuilder sb = new StringBuilder();

        for (Point point : result) {
            sb.append("https://www.google.ch/maps/@");
            sb.append(point.getX());
            sb.append(",");
            sb.append(point.getY());
            sb.append(",8z\n");
        }
//        System.out.println(sb);
    }
}