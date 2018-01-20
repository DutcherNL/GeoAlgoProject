package Space;

public class Line extends Segment{

	public Line(Vertex start, Vertex end) {
		super(start, end);
	}

	public Vertex intersect(Segment segment) {
		if (this.isVertical() && segment.isVertical()) {
			if (startVertex.x == segment.getStartVertex().x) {
				return null;
			} else {
				return null;
			}
		} else if (this.isVertical()) {
			LineFormula formula = new LineFormula(segment.startVertex, segment.endVertex);

			double x = this.getStartVertex().x;
			double y = formula.getY(x);

			if ((segment.startVertex.y <= y && y <= segment.endVertex.y) || (segment.startVertex.y >= y && y >= segment.endVertex.y)) {
				return new Vertex(x, y);
			}
		} else if (segment.isVertical()) {
			LineFormula formula = new LineFormula(this.startVertex, this.endVertex);

			double x = segment.getStartVertex().x;
			double y = formula.getY(x);

			return new Vertex(x, y);
		} else {
			LineFormula f1 = new LineFormula(this.startVertex, this.endVertex);
			LineFormula f2 = new LineFormula(segment.startVertex, segment.endVertex);

			if (f1.a == f2.a) {
				if (f1.b == f2.b) {
                    return new Vertex(segment.getEndVertex().x, segment.getEndVertex().y);
                }
			}

			double x = (f2.b - f1.b)/(f1.a - f2.a);
			double y = f1.getY(x);

			return new Vertex(x, y);
		}
		return null;
	}

	private class LineFormula {
		double a;
		double b;

		LineFormula(Vertex v1, Vertex v2) {
			double dy = v1.getY() - v2.getY();
			double dx = v1.getX() - v2.getX();

            a = dy/dx;

            b = v1.getY() - (a * v1.getX());
		}

		public double getY(double x) {
			return a*x + b;
		}

		public int getSign(Vertex v) {
			return (int) Math.signum(v.getY() - a * v.getX() - b);
		}
	}
}
