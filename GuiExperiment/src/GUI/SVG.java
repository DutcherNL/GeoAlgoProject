package GUI;

import GUI.DrawSpace.JPanel_DrawSpace;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SVG {
    private JPanel_DrawSpace space;

    public SVG(JPanel_DrawSpace space) {
        this.space = space;
    }

    public void export() {
        // Get a DOMImplementation.
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        JPanel_DrawSpace.DEBUG = false;
        space.paint(svgGenerator);
        JPanel_DrawSpace.DEBUG = true;

        Writer out = null;
        try {
            out = new FileWriter("export.svg");
            svgGenerator.stream(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}