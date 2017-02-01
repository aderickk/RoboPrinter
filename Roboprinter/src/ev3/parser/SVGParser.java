/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ev3.parser;
 
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument; 
/**
 *
 * @author David Garcia
 */
public class SVGParser { 

    String SVGNS =  SVGDOMImplementation.SVG_NAMESPACE_URI;

    /**
     * The number of points computed during curve interpolation per unit of
     * linear pixel length. For example, if a a path is 10px in length, and
     * `DENSITY` is set to 2, the path will be rendered with 20 points.
     *
     * @public
     */
    final int DENSITY = 1;

    /**
     * The number of decimal places computed during curve interpolation when
     * generating points for `<circle>`, `<ellipse>`, and `<path>` elements.
     *
     * @public
     */
    final int PRECISION = 3;

    ArrayList<ElsNode> els = new ArrayList();
    private int height=0;
    private int width=0;

    public Document loadFile(String path) {
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            File file = new File(path);
            String uri = file.toURI().toString(); 
            return f.createDocument(uri); 
        } catch (IOException ex) {
            Logger.getLogger(SVGParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * SVG => WKT.
     *
     * @param {String} svg: SVG markup.
     * @return {String}: Generated WKT.
     *
     * @public
     */
    public void convert(Document svg) {
        
        NodeList svgFile = svg.getElementsByTagName("svg"); 
        
        width = Integer.parseInt(svgFile.item(0).getAttributes().getNamedItem("width").getNodeValue().replace("px", "").trim());
        height = Integer.parseInt(svgFile.item(0).getAttributes().getNamedItem("height").getNodeValue().replace("px", "").trim());  

        // Match <polygon> elements.
        NodeList polygon = svg.getElementsByTagName("polygon");
        for (int i = 0; i < polygon.getLength(); i++) {
            String points = polygon.item(i).getAttributes().getNamedItem("points").getNodeValue();
            els.add(polygon(points));
        }

        // Match <polyline> elements.
        NodeList polyline = svg.getElementsByTagName("polyline");
        for (int i = 0; i < polyline.getLength(); i++) { 
            String points = polyline.item(i).getAttributes().getNamedItem("points").getNodeValue(); 
            els.add(polyline(points));
        }

        // Match <line> elements.
        NodeList line = svg.getElementsByTagName("line");
        for (int i = 0; i < line.getLength(); i++) {
            float x1 = Float.valueOf(line.item(i).getAttributes().getNamedItem("x1").getNodeValue());
            float y1 = Float.valueOf(line.item(i).getAttributes().getNamedItem("y2").getNodeValue());
            float x2 = Float.valueOf(line.item(i).getAttributes().getNamedItem("x2").getNodeValue());
            float y2 = Float.valueOf(line.item(i).getAttributes().getNamedItem("y2").getNodeValue());
            els.add(line(x1, y1, x2, y2));
        }

        // Match <rect> elements.
        NodeList rect = svg.getElementsByTagName("rect");
        for (int i = 0; i < rect.getLength(); i++) {
            float x = Float.valueOf(rect.item(i).getAttributes().getNamedItem("x").getNodeValue());
            float y = Float.valueOf(rect.item(i).getAttributes().getNamedItem("y").getNodeValue());
            float width = Float.valueOf(rect.item(i).getAttributes().getNamedItem("width").getNodeValue());
            float height = Float.valueOf(rect.item(i).getAttributes().getNamedItem("height").getNodeValue());
            els.add(rect(x, y, width, height));
        }

        // Match <circle> elements.
        NodeList circle = svg.getElementsByTagName("circle");
        for (int i = 0; i < circle.getLength(); i++) {
            float cx = Float.valueOf(circle.item(i).getAttributes().getNamedItem("cx").getNodeValue());
            float cy = Float.valueOf(circle.item(i).getAttributes().getNamedItem("cy").getNodeValue());
            float r = Float.valueOf(circle.item(i).getAttributes().getNamedItem("r").getNodeValue());
            els.add(circle(cx, cy, r));
        }

        // Match <ellipse> elements.
        NodeList ellipse = svg.getElementsByTagName("ellipse");
        for (int i = 0; i < ellipse.getLength(); i++) {
            float cx = Float.valueOf(ellipse.item(i).getAttributes().getNamedItem("cx").getNodeValue());
            float cy = Float.valueOf(ellipse.item(i).getAttributes().getNamedItem("cy").getNodeValue());
            float rx = Float.valueOf(ellipse.item(i).getAttributes().getNamedItem("rx").getNodeValue());
            float ry = Float.valueOf(ellipse.item(i).getAttributes().getNamedItem("ry").getNodeValue());
            els.add(ellipse(cx, cy, rx, ry));
        }

        // Match <path> elements.
//        NodeList path = svg.getElementsByTagName("path");
//        for (int i = 0; i < path.getLength(); i++) {
//            String d = path.item(i).getAttributes().getNamedItem("d").getNodeValue();
//            els.add(path(d));
//        }
    }

    /**
     * Construct a WKT line from SVG start/end point coordinates.
     *
     * @param {Number} x1: Start X.
     * @param {Number} y1: Start Y.
     * @param {Number} x2: End X.
     * @param {Number} y2: End Y.
     * @return {String}: Generated WKT.
     *
     * @public
     */
    public ElsNode line(float x1, float y1, float x2, float y2) {
        return new ElsNode("drawLine", "\""+x1 + " " + y2 + "\",\"" + x2 + " " + y2+"\"");
    }

    /**
     * Construct a WKT linestrimg from SVG `points` attribute value.
     *
     * @param {String} points: <polyline> points attribute value.
     * @return {String}: Generated WKT.
     *
     * @public
     */
    public ElsNode polyline(String points) {
        String[] pts = points.trim().split(" ");
        points = "";
        for (int i = 0; i < pts.length; i++) {
            if (!pts[i].isEmpty()) {
                points += "\"" + pts[i].replaceAll(",", " ") + "\",";
            }
        }
        points = points.substring(0, points.length()-1);
        return new ElsNode("drawPolyline", points);
    }

    /**
     * Construct a WKT polygon from SVG `points` attribute value.
     *
     * @param {String} points: <polygon> `points` attribute value.
     * @return {String}: Generated WKT.
     *
     * @public
     */
    public ElsNode polygon(String points) {
        String[] pts = points.trim().split(" ");
        points = "";
        for (int i = 0; i < pts.length; i++) {
            if (!pts[i].isEmpty()) {
                points += "\"" + pts[i].replaceAll(",", " ").trim() + "\",";
            }
        }
        points += "\"" + pts[0].replaceAll(",", " ") + "\"";
        return new ElsNode("drawPolygon", points);
    }

    /**
     * Construct a WKT polygon from SVG rectangle origin and dimensions.
     *
     * @param {Number} x: Top left X.
     * @param {Number} y: Top left Y.
     * @param {Number} width: Rectangle width.
     * @param {Number} height: Rectangle height.
     * @return {String}: Generated WKT.
     *
     * @public
     */
    public ElsNode rect(float x, float y, float width, float height) {
        String[] pts = new String[5];
        pts[0] = "\"" + x + " " + y + "\"";
        pts[1] = "\"" + (x + width) + " " + y + "\"";
        pts[2] = "\"" + (x + width) + " " + (y + height) + "\"";
        pts[3] = "\"" + x + " " + (y + height) + "\"";
        pts[4] = "\"" + x + " " + y + "\"";
        return new ElsNode("drawRect", arrayToString(pts));
    }

    /**
     * Construct a WKT polygon for a circle from origin and radius.
     *
     * @param {Number} cx: Center X.
     * @param {Number} cy: Center Y.
     * @param {Number} r: Radius.
     * @return {String} wkt: Generated WKT.
     *
     * @public
     */
    public ElsNode circle(float cx, float cy, float r) {
        // Compute number of points.
        Double circumference = Math.PI * 2.0 * r;
        Double point_count = __round(circumference * this.DENSITY);

        // Compute angle between points.
        Double interval_angle = 360 / point_count;

        String[] pts = new String[point_count.intValue() + 1];

        // Genrate the circle.
        for (int i = 0; i < point_count; i++) {
            double angle = (interval_angle * i) * (Math.PI / 180);
            double x = __round(cx + r * Math.cos(angle));
            double y = __round(cy + r * Math.sin(angle));
            pts[i] = "\"" + x + " " + y + "\"";
        }
        pts[point_count.intValue()] = pts[0];
        return new ElsNode("drawCircle", arrayToString(pts));
    }

    /**
     * Construct a WKT polygon for an ellipse from origin and radio.
     *
     * @param {Number} cx: Center X.
     * @param {Number} cy: Center Y.
     * @param {Number} rx: Horizontal radius.
     * @param {Number} ry: Vertical radius.
     * @return {String} wkt: Generated WKT.
     *
     * @public
     */
    public ElsNode ellipse(float cx, float cy, float rx, float ry) {
        // Approximate the circumference.
        Double circumference = 2 * Math.PI * Math.sqrt(
                (Math.pow(rx, 2) + Math.pow(ry, 2)) / 2);

        // Compute number of points and angle between points.
        Double point_count = __round(circumference * DENSITY);
        Double interval_angle = 360 / point_count;
        String[] pts = new String[point_count.intValue() + 1];

        // Genrate the circle.
        for (int i = 0; i < point_count; i++) {
            double angle = (interval_angle * i) * (Math.PI / 180);
            double x = __round(cx + rx * Math.cos(angle));
            double y = __round(cy + ry * Math.sin(angle));
            pts[i] = "\"" + x + " " + y + "\"";
        }

        pts[point_count.intValue()] = pts[0];
        return new ElsNode("drawEllipse", arrayToString(pts));
    }

    /**
     * Construct a WKT polygon from a SVG path string. Approach from:
     * http://whaticode.com/2012/02/01/converting-svg-paths-to-polygons/
     *
     * @param {String} d: <path> `d` attribute value.
     * @return {String}: Generated WKT.
     *
     * @public
     */
    public ElsNode path(String d) {
        // Try to extract polygon paths closed with 'Z'.
        Pattern pattern = Pattern.compile("([^z|Z]+[z|Z])");
        Matcher matcher = pattern.matcher(d.trim());
        ArrayList<Element> polys = new ArrayList();
        while (matcher.find()) {
            polys.add(pathElement(matcher.group().trim() + "z"));
        }

        // If closed polygon paths exist, construct a `drawLine`.
        if (!polys.isEmpty()) {
            ArrayList<String> parts = new ArrayList();
            for (Element element : polys) {
                parts.add(pathPoints(element, true));
            }
            return new ElsNode("drawLine", arrayToString(parts.toArray()));
        } // Otherwise, construct a `drawLine` from the unclosed path.
        else {
            Element line = pathElement(d);
            return new ElsNode("drawLine", pathPoints(line, false));
        }

    }
    
    public String arrayToString(Object[] a){
        return Arrays.toString(a).replaceAll("[\\[\\]]", "");
    }

    /**
     * Construct a SVG path element.
     *
     * @param {String} d: <path> `d` attribute value.
     * @return {SVGPathElement}: The new <path> element.
     *
     * @private
     */
    private Element pathElement(String d) {
//        DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
//        SVGDocument doc = (SVGDocument)domImpl.createDocument(SVGNS, "svg", null);
//        Element svgRoot = doc.getDocumentElement();
//        Element element = doc.createElementNS(SVGNS, "path"); 
//        element.setAttributeNS(SVGNS,"d", d);
//        svgRoot.appendChild(element);
//
//        
//        svgRoot.getChildNodes().getLength();
//        System.out.println(((SVGOMPathElement)svgRoot.getChildNodes().item(0)).getTotalLength());
//         
        
        SVGDOMImplementation impl = (SVGDOMImplementation) SVGDOMImplementation.getDOMImplementation();
        
        SVGDocument document =  (SVGDocument)impl.createDocument(SVGNS, "svg", null);
        Element root = document.getDocumentElement();
        root.setAttributeNS(SVGNS, "d", d); 
//        ((SVGPathElement)root).getTotalLength();
        
        
        return root;
    }

    /**
     * Construct a SVG path element.
     *
     * @param {SVGPathElement} path: A <path> element.
     * @param {Boolean} closed: True if the path should be closed.
     * @return array: An array of space-delimited coords.
     *
     * @private
     */
    private String pathPoints(Element path, boolean closed) {

        ArrayList<String> pts = new ArrayList();
        // Get number of points. 

        if (closed) { 
            //((SVGOMPathElement)path).getTotalLength(); 
        } 
        return null;
    }

    /**
     * Round a number to the number of decimal places in `PRECISION`.
     *
     * @param {Number} val: The number to round.
     * @return {Number}: The rounded value.
     *
     * @private
     */
    private double __round(double val) {
        double root = Math.pow(10, PRECISION);
        return Math.round(val * root) / root;
    }

    public ArrayList<ElsNode> getStatements() { 
        return els;
    }
    
    public Dimension getSVGDimension(){
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        SVGParser parser = new SVGParser();
        Document doc = parser.loadFile("C:\\Users\\user\\Google Drive\\MCP\\triangle2.svg");
        parser.convert(doc);
        System.out.println(parser.getStatements().toString());  
        
        
    }

}
