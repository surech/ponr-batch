package ch.poinzofnoreturn.batch;

import ch.poinzofnoreturn.batch.model.rest.PoinzProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections4.iterators.ArrayIterator;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by surech on 12.01.16.
 */
public class ProviderReader implements ItemReader<PoinzProvider> {

    /**
     * Gibt an, in welchen Schritten in Meter in die Schweiz abgefragt werden soll
     */
    private final static int SCAN_DELTA = 100000;

    /**
     * Factory zum Erstellen von geografischen Punkten
     */
    private final static GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    /** Template für die Abfrage-URL */
    private final static String URL = "https://admin.poinz.ch/api/locations?categories=restaurant,cafe,takeaway,shopping,services,leisure,wellness&location=%s,%s&search_pattern=";

    /** Schnittstelle für den Aufruf der REST-API */
    private final RestTemplate template = new RestTemplate();

    /** Stack mit den abzuarbeitenden Punkten */
    private final Stack<Point> grid;

    /** Iterator über die gelandene Provider */
    private ArrayIterator<PoinzProvider> iterator = null;

    public ProviderReader() {
        // Liste mit den Parametern erstellen
        grid = this.buildScanGrid();
    }

    /**
     * Wird nur im Test aufgerufen um die ganze Initialisierung zu umgehen.
     *
     * @param test Sinnloser Parameter
     */
    protected ProviderReader(boolean test) {
        grid = new Stack<>();
    }

    protected PoinzProvider[] loadProvider(Point point) {
        // URL erstellen
        String requestUrl = String.format(URL, point.getX(), point.getY());

        // Header-Informationen
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Key", "7e6e888b9f407ab1f1741bfeb97a6ab1");
        headers.set("X-Device-Id", "00018445F5D2-0F77-4BC6-BC5B-501F5514D349");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<PoinzProvider[]> response = template.exchange(requestUrl, HttpMethod.GET, entity, PoinzProvider[].class);
        return response.getBody();
    }

    /**
     * Erstellt eine Liste mit Koordinaten, welche die ganze Schweiz abdecken
     * @return Liste mit Koordinaten
     */
    protected Stack<Point> buildScanGrid() {
        // Ausgangsposition berechnen
        Coordinate root = new Coordinate(47.808697, 5.9688235);
        Point start = GEOMETRY_FACTORY.createPoint(root);

        Stack<Point> grid = new Stack<>();

        for (int x = 0; x < 220100; x += SCAN_DELTA) {
            for (int y = 0; y < 348400; y += SCAN_DELTA) {
                Point point = move(start, -x, -y);
                grid.push(point);
            }
        }

        return grid;
    }

    /**
     * Verschiebt einen geografischen Punkt
     *
     * @param start Ausgangslage
     * @param x     Latitude
     * @param y     Longitude
     * @return Resultat
     */
    protected Point move(Point start, int x, int y) {
        // x-Delta in Metern berechnen
        double xDelta = x / 111111.0;
        double xResult = start.getX() + xDelta;

        // y-Delta berechnen
        double yDelta = y / (111111.0 * Math.cos(xResult));
        double yResult = start.getY() + yDelta;

        Coordinate coordinate = new Coordinate(xResult, yResult);
        return GEOMETRY_FACTORY.createPoint(coordinate);
    }

    @Override
    public PoinzProvider read() throws Exception {
        if (iterator == null || (!iterator.hasNext() && !grid.isEmpty())) {
            // Betreiber laden
            PoinzProvider[] provider = loadProvider(grid.pop());
            this.iterator = new ArrayIterator<PoinzProvider>(provider);
        }

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }
}
