package org.newdawn.slick.svg.inkscape;

import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.svg.Diagram;
import org.newdawn.slick.svg.Figure;
import org.newdawn.slick.svg.Loader;
import org.newdawn.slick.svg.NonGeometricData;
import org.newdawn.slick.svg.ParsingException;
import org.w3c.dom.Element;

/**
 * Processor for <ellipse> and <path> nodes marked as arcs
 *
 * @author kevin
 */
public class EllipseProcessor implements ElementProcessor {
	
	/**
	 * @see org.newdawn.slick.svg.inkscape.ElementProcessor#process(org.newdawn.slick.svg.Loader, org.w3c.dom.Element, org.newdawn.slick.svg.Diagram)
	 */
	public void process(Loader loader, Element element, Diagram diagram) throws ParsingException {
		Transform transform = Util.getTransform(element);
		float x = Util.getFloatAttribute(element,"cx");
		float y = Util.getFloatAttribute(element,"cy");
		float rx = Util.getFloatAttribute(element,"rx");
		float ry = Util.getFloatAttribute(element,"ry");
		
		Ellipse ellipse = new Ellipse(x,y,rx,ry);
		Shape shape = ellipse.transform(transform);

		NonGeometricData data = Util.getNonGeometricData(element);
		
		diagram.addFigure(new Figure(shape, data));
	}

	/**
	 * @see org.newdawn.slick.svg.inkscape.ElementProcessor#handles(org.w3c.dom.Element)
	 */
	public boolean handles(Element element) {
		if (element.getNodeName().equals("ellipse")) {
			return true;
		}
		if (element.getNodeName().equals("path")) {
			if (element.getAttributeNS(Util.SODIPODI, "type").equals("arc")) {
				return true;
			}
		}
		
		return false;
	}

}
