
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GraphUserStudy {
    private int padding = 30;
    private int labelPadding = 25;
    private Color gridColor = new Color(200, 200, 200, 200);
    private static int pointWidth;
    private static int numberYDivisions;
    private static int scores;
    private static List<String> xAxisTemp;
    private static List<String> yAxisTemp;
    private static List<String> xAxisStocks;
    private static List<String> xAxis;
    private static List<String> yAxis;
    public static void main(String[] args) {
    	populateXYAxisDataForTemp();
//    	populateXYDataForStocks();
        new GraphUserStudy();
    }
    
    public static void generateYAxisPrice(float start, float end, float increment){
    	yAxis= new ArrayList<String>();
    	int i=0;
    	double t= start;
    	while(t<end){
    		t=  (start+ i*increment);
    		t= (double)Math.round(t * 100000d) / 100000d;
    		DecimalFormat df = new DecimalFormat("#.###");
    		df.format(t);
    		yAxis.add("$ "+t);
    		i++;
    	}
    }
    
    public static void generateYAxisTemp(float start, float end, float increment){
    	yAxis= new ArrayList<String>();
    	int i=0;
    	double t= start;
    	while(t<=end){
    		t=  (start+ i*increment);
    		t= (double)Math.round(t * 100000d) / 100000d;
    		yAxis.add(t+" ºc");
    		i++;
    	}
    }
    
    static void populateXYAxisDataForTemp(){
    	xAxisTemp= new ArrayList<String>();
    	populateXAxisTemp(9,12,3, 1);
    	yAxisTemp= new ArrayList<String>();
    	populateYAxis(0, 200, 20);
    	pointWidth = 1;
    	numberYDivisions = yAxisTemp.size()-1;
    	scores= xAxisTemp.size();
    	xAxis= xAxisTemp;
    	yAxis= yAxisTemp;
    }
    
	static List<String> populateXAxisTemp(int start, int end, int interval, int time) {
		for (int k = 0; k < 2; k++) {
			for (int i = start; i <= end; i = i + interval) {
				xAxisTemp.add(i + "");
			}
		}
		return xAxisTemp;
	}
	
	static List<String> populateYAxis(double start, double end, double interval){
			for (double i = start; i <= end; i = i + interval) {
				yAxisTemp.add(i + "");
		}
		return yAxisTemp;
	}
    
    static void populateXYDataForStocks(){
    	xAxisStocks= new ArrayList<String>();
    	xAxisStocks.add("10 AM");
    	xAxisStocks.add("12 AM");
    	xAxisStocks.add("2 AM");
    	xAxisStocks.add("4 AM");
    	pointWidth = 1;
    	xAxis= xAxisStocks;
    	generateYAxisPrice(68.2f, 69.0f, 0.2f);
    	numberYDivisions = yAxis.size()-1;
    	scores= xAxisStocks.size();
    }
   
    public GraphUserStudy() {
    	 List<Double> scores = new ArrayList<>();
         Random random = new Random();
         int maxDataPoints = 8;
         int maxScore = 14;
         for (int i = 0; i < maxDataPoints; i++) {
             scores.add((double) random.nextDouble() * maxScore);
         }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("User Experiement: Time Series Data Graph");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                TestPane pane= new TestPane();
                frame.add(pane);
                frame.pack();
                frame.setLocationRelativeTo(pane);
                frame.setVisible(true);
            }
        });
    }
    
    public class TestPane extends JPanel {

        private List<List<Point>> points;
        public TestPane() {
        	
            points = new ArrayList<>(25);
            MouseAdapter ma = new MouseAdapter() {

                private List<Point> currentPath;

                @Override
                public void mousePressed(MouseEvent e) {
                    currentPath = new ArrayList<>(25);
                    currentPath.add(e.getPoint());

                    points.add(currentPath);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    Point dragPoint = e.getPoint();
                    currentPath.add(dragPoint);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    currentPath = null;
                }

            };

            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(900, 620);
        }
        
       

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
            g2d.setColor(Color.BLACK);
            for (int i = 0; i < numberYDivisions+1; i++) {
                int x0 = padding + labelPadding;
                int x1 = pointWidth + padding + labelPadding;
                int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
                int y1 = y0;
                if (scores > 0) {
                    g2d.setColor(gridColor);
                    g2d.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                    g2d.setColor(Color.BLACK);
                    String yLabel = yAxis.get(i)+"";
                    FontMetrics metrics = g2d.getFontMetrics();
                    int labelWidth = metrics.stringWidth(yLabel);
                    g2d.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
                }
                g2d.drawLine(x0, y0, x1, y1);
            }
            
            for (int i = 0; i < scores; i++) {
                if (scores > 1) {
                    int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores - 1) + padding + labelPadding;
                    int x1 = x0;
                    int y0 = getHeight() - padding - labelPadding;
                    int y1 = y0 - pointWidth;
                    if ((i % ((int) ((scores / 20.0)) + 1)) == 0) {
                        g2d.setColor(gridColor);
                        g2d.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                        g2d.setColor(Color.BLACK);
                        String xLabel = xAxis.get(i) + "";
                        FontMetrics metrics = g2d.getFontMetrics();
                        int labelWidth = metrics.stringWidth(xLabel);
                        g2d.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                    }
                    g2d.drawLine(x0, y0, x1, y1);
                }
            }
            
            g2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
            g2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

            g2d.setColor(Color.BLUE);
            for (List<Point> path : points) {
                Point from = null;
                for (Point p : path) {
                    if (from != null) {
                        g2d.drawLine(from.x, from.y, p.x, p.y);
                    }
                    from = p;
                }
            }
            g2d.dispose();
        }

    }

}