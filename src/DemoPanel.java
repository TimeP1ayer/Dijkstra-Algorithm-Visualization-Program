import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DemoPanel extends JFrame {
    /**
     * 演示路径运行
     */
    int totalstep;//总共步数
    int step;//当前步数

    private ArrayList<MyPoint> blackPoints = new ArrayList<>();//保存点，用来绘制画面
    private ArrayList<String> pointNames = new ArrayList<>();//保存点的名字，用来绘制画面
    private ArrayList<MyPoint[]> blackLines = new ArrayList<>();//保存线，用来绘制画面
    ArrayList<MyPoint[]> blueLines = new ArrayList<>();//保存蓝线，用来绘制画面
    ArrayList<MyPoint[]> redLines = new ArrayList<>();//保存红线，用来绘制画面

    private int lineturn = 0;
    private int bluelineturn = 0;
    private int redlineturn = 0;

    private JButton back = new JButton("back");
    private JButton next = new JButton("next");
    private JButton illustrate = new JButton("说明");
    private JPanel canvas;

    public void initByKey(AdjacencyMatrix a ,Dijkstra dijkstra){
        this.blackPoints=a.getPoints();
        this.pointNames=a.pointNames;
        this.blackLines=a.lines;

        toDraw toDraw = new toDraw(dijkstra,blackPoints);
        toDraw.init();
        this.blueLines=toDraw.blueLines;
        this.redLines=toDraw.redLines;
        this.totalstep=toDraw.getTotalPathLength();
    }

    public void init(PointAndLineDrawer pad,Dijkstra dijkstra){
        //接收原图参数
        this.blackPoints=pad.getPoints();
        this.pointNames=pad.getPointNames();
        this.blackLines =pad.getLines();

        //接收遍历参数
        toDraw toDraw = new toDraw(dijkstra,blackPoints);
        toDraw.init();
        this.blueLines=toDraw.blueLines;
        this.redLines=toDraw.redLines;
        this.totalstep=toDraw.getTotalPathLength();
        //检查参数
        //checkInit();
    }
    public void checkInit(){
        System.out.println();
        System.out.println("Panel check:");
        System.out.println("black:");
        for(MyPoint p:blackPoints){
            System.out.print(p.name+" ");
        }
        System.out.println();

        System.out.println("blue:");
        for(MyPoint[] p:blueLines){
            System.out.print(p[0].name+" "+p[1].name+";");
        }
        System.out.println();

        System.out.println("red:");
        for(MyPoint[] p:redLines){
            System.out.print(p[0].name+" "+p[1].name+";");
        }
        System.out.println();
        System.out.println("check finish");
        System.out.println();
    }

    public DemoPanel(){
        setTitle("Dijkstra Demo "+"step:totalstep"+  step +":"+totalstep +"  blue:"+blueLines.size()+"  red:"+redLines.size());
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttonPanel.add(back);
        buttonPanel.add(next);
        buttonPanel.add(illustrate);
        add(buttonPanel,BorderLayout.NORTH);

        canvas = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                drawOrigin(g);//绘制原图
                drawBlue(g);//绘制蓝线
                drawRed(g);//绘制红线
            }
        };

        getContentPane().add(canvas);

        back.addActionListener(e -> {
            if (step-1>=0)
            step--;
            refeshTittle(this);
            repaint();
        });

        next.addActionListener(e -> {
            if (step+1<=totalstep)
            step++;
            refeshTittle(this);
            repaint();
        });

        illustrate.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,"说明\n" +
                    "点击next进行下一步搜寻，为蓝色的线\n" +
                    "点击back回到上一步\n" +
                    "最后遍历完成后，红色的路线即为最短路径");
        });
    }

    /**
     * 把原图画出来
     */
    void drawOrigin(Graphics g){
        for (int i = 0; i < blackPoints.size(); i++) {
            MyPoint p = blackPoints.get(i);
            g.fillOval(p.x - 5, p.y - 5, 10, 10);
            g.drawString(pointNames.get(i), p.x + 10, p.y - 10);
        }
        for (MyPoint[] line : blackLines) {
            drawArrowLine(Color.BLACK,g, line[0],line[1] , 10, 5,lineturn);
            lineturn++;
        }
        lineturn=0;
    }

    void refeshTittle(DemoPanel demoPanel){
        demoPanel.setTitle("Dijkstra Demo "+"step:totalstep"+  step +":"+totalstep +"  blue:"+blueLines.size()+"  red:"+redLines.size());
    }

    /**
     * 把遍历路径画出来
     */
    void drawBlue(Graphics g){
        if(step!=0){
            //根据step绘制蓝线
            for (MyPoint[] line : blueLines) {
                drawArrowLine(Color.BLUE,g, line[0],line[1] , 10, 5,step);
                bluelineturn++;
                if(bluelineturn==step){
                    break;
                }
            }
            bluelineturn=0;
        }
    }

    /**
     * 把最终路径标出来
     */
    void drawRed(Graphics g){
        //根据step绘制红线
        if(step==totalstep){
            for (MyPoint[] line : redLines) {
                drawArrowLine(Color.RED,g, line[0],line[1] , 10, 5,redlineturn);
                redlineturn++;
            }
            redlineturn=0;
        }
    }

    /**
     * 画线
     */
    private void drawArrowLine( Color color,Graphics g, MyPoint start, MyPoint end, int d, int h, int lineturn) {
        /**
         * 自连
         */
        int x1 = start.x;
        int y1 = start.y;

        int x2 = end.x;
        int y2 = end.y;


        int dx = x2 - x1;
        int dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d;
        double xn = xm;
        double ym = h;
        double yn = -h;
        double x;
        double sin = dy / D;
        double cos = dx / D;
        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.setColor(color);

        Graphics2D g2d = (Graphics2D) g;
        Stroke stroke = new BasicStroke(3);
        g2d.setStroke(stroke);


        g2d.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

}
