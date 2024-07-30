import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
//1

/**
 * 仍然需要：
 * 输出算法结果
 */
public class PointAndLineDrawer extends JFrame {
    private ArrayList<MyPoint> points = new ArrayList<>();//保存点，用来绘制画面
    private ArrayList<String> pointNames = new ArrayList<>();//保存点的名字，用来绘制画面
    private ArrayList<MyPoint[]> lines = new ArrayList<>();//保存线，用来绘制画面
    private ArrayList<Integer>linelength =new ArrayList<>();//按lines的顺序保存线条长度
    private ArrayList<Connection> LinesAndDistance = new ArrayList<>();//作为向量，存储起点，终点，长度，传给矩阵初始化

    private MyPoint startPoint = null;//起点
    private MyPoint endPoint = null;//终点
    private int lineturn = 0;

    private int PointTurn=0;//点的个数，传给矩阵初始化，id从0开始


    private JButton finish = new JButton("结束图形绘制");
    private JButton keyinput = new JButton("从键盘输入邻接矩阵");
    private JButton help = new JButton("帮助");
    private JButton clearALL = new JButton("清空画布");
    private JPanel canvas;
    private int reset;

    public PointAndLineDrawer() {

        setTitle("Point Drawer");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttonPanel.add(finish);
        buttonPanel.add(clearALL);
        buttonPanel.add(keyinput);
        buttonPanel.add(help);

        this.add(buttonPanel,BorderLayout.NORTH);

        //finish监听器
        finish.addActionListener(e -> {
            AdjacencyMatrix a = new AdjacencyMatrix();
            if(PointTurn==0){
                JOptionPane.showMessageDialog(this, "仍未有点与线被绘制在图中!");
            }else if (PointTurn==1){
                JOptionPane.showMessageDialog(this, "当前图中只有一个点，不足以演示算法。");
            }else{
                a.init(PointTurn,points,LinesAndDistance);
                JOptionPane.showMessageDialog(this, "Adjacency Matrix:\n" + a.MatrixString());
                Dijkstra dijkstra = new Dijkstra(a);
                dijkstra.run();
                if(dijkstra.result){
                    DemoPanel demoPanel = new DemoPanel();
                    demoPanel.init(this,dijkstra);
                    SwingUtilities.invokeLater(() -> demoPanel.setVisible(true));
                }else{
                    JOptionPane.showMessageDialog(this,"没有可通行路径");
                }
            }
        });

        //清空
        clearALL.addActionListener(e -> {
            resetParams();
            canvas.removeAll();
            canvas.repaint();
        });

        //从键盘输入
        keyinput.addActionListener(e -> {
            AdjacencyMatrix a = new AdjacencyMatrix();
            canvas.removeAll();
            if(a.initByKey(this)){
                JOptionPane.showMessageDialog(this, "Adjacency Matrix:\n" + a.MatrixString());
                Dijkstra dijkstra = new Dijkstra(a);
                dijkstra.run();
                if(dijkstra.result){
                    DemoPanel demoPanel = new DemoPanel();
                    demoPanel.initByKey(a,dijkstra);
                    SwingUtilities.invokeLater(() -> demoPanel.setVisible(true));
                }else{
                    JOptionPane.showMessageDialog(this,"没有可通行路径");
                }
            }
        });

        help.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Dijkstra算法演示:最短路径寻找算法" +
                    "使用方法\n" +
                    "鼠标左键:创建一个点\n" +
                    "鼠标右键:分别选中两个点后,将会对两个点进行连线操作,线的方向指向最后一个选中的点");
        });

        //画布
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 读取并绘制点
                for (int i = 0; i < points.size(); i++) {
                    MyPoint p = points.get(i);
                    g.fillOval(p.x - 5, p.y - 5, 10, 10);
                    g.drawString(pointNames.get(i), p.x + 10, p.y - 10);
                }
                // 读取并绘制线
                for (MyPoint[] line : lines) {
                    g.setColor(Color.RED);
                    drawArrowLine(g, line[0], line[1], 10, 5,lineturn);
                    lineturn++;
                }
                lineturn=0;
                //被选中后变为红色
                clickedPoint(startPoint,g,Color.RED);
                clickedPoint(endPoint,g,Color.RED);
                if(reset==1){
                    resetClick(g);
                    reset=0;
                }
            }
        };

        //画布监听器
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //鼠标左键：画点功能，并添加点的名字
                if (e.getButton() == MouseEvent.BUTTON1) {
                    startPoint = null;
                    endPoint = null;

                    String name = JOptionPane.showInputDialog("输入点的名字:");
                    if(name==null){
                        return;
                    }
                    MyPoint p = MyPoint.Clone(e.getPoint(),name,PointTurn);//补充点p的信息,id从0开始

                    points.add(p);
                    pointNames.add(name != null ? name : "");
                    PointTurn++;//点数+1
                    canvas.repaint();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //鼠标右键：选择点画线功能
                    for (MyPoint p : points) {
                        if (p.distance(e.getPoint()) <= 5) {
                            //先后赋值起点和终点
                            if (startPoint == null) {
                                //在点组合中寻找该点
                                startPoint = MyPoint.findByPoint(e.getPoint(),points);
                            } else {
                                endPoint = MyPoint.findByPoint(e.getPoint(),points);
                                if(startPoint==endPoint){
                                    JOptionPane.showMessageDialog(null,"不能选中同一个点");
                                    startPoint = null;
                                    endPoint = null;
                                    return;
                                }
                                //连线，并添加到line数组里
                                lines.add(new MyPoint[]{startPoint, endPoint});
                                String distance = JOptionPane.showInputDialog("输入线的长度:");
                                System.out.println("connect:"+startPoint.name+"->"+endPoint.name+"  length:"+distance);
                                System.out.println();
                                if (startPoint==null||endPoint==null){
                                    reset=1;
                                    return;
                                }
                                Connection c = new Connection(startPoint,endPoint,Integer.parseInt(distance));
                                linelength.add(Integer.parseInt(distance));
                                //距离信息
                                LinesAndDistance.add(c);
                                //重置起始点和终点，画线结束
                                reset=1;
                            }
                            canvas.repaint();
                            break;
                        }
                    }
                }
            }
        });

        getContentPane().add(canvas);
    }
    //绘制有箭头的直线
    private void drawArrowLine(Graphics g, MyPoint start,MyPoint end, int d, int h,int lineturn) {
        /**
         * 自连
         */

        int x1=start.x;
        int x2=end.x;
        int y1=start.y;
        int y2=end.y;

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

        Graphics2D g2d = (Graphics2D) g;
        Stroke stroke = new BasicStroke(3);
        g2d.setStroke(stroke);


        g2d.drawLine(x1, y1, x2, y2);
        g.drawString(String.valueOf(linelength.get(lineturn)), (x1+x2)/2+5, (y1+y2)/2-5);
        g.setColor(Color.BLUE);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    //被选中后变为红色
    private void clickedPoint(MyPoint p,Graphics g,Color color){
        if(p!=null&&p.x!=0&&p.y!=0){
            g.setColor(color);
            g.fillOval(p.x - 5, p.y - 5, 10, 10);
        }
    }

    private void resetClick(Graphics g){
        clickedPoint(startPoint,g,Color.BLACK);
        clickedPoint(endPoint,g,Color.black);
        startPoint=null;
        endPoint=null;
        repaint();
    }
    //参数重置
    private void resetParams(){
        /**
         * 重置参数
         */
        this.points = new ArrayList<>();
        this.pointNames = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.linelength = new ArrayList<>();
        this.startPoint = new MyPoint();
        this.endPoint = new MyPoint();
        this.lineturn = 0;
        this.PointTurn = 0;
        this.reset=1;
        this.LinesAndDistance = new ArrayList<>();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PointAndLineDrawer().setVisible(true));
    }

    public ArrayList<MyPoint> getPoints() {
        return points;
    }
    public ArrayList<String> getPointNames() {
        return pointNames;
    }
    public ArrayList<MyPoint[]> getLines() {
        return lines;
    }
    public ArrayList<Integer> getLinelength() {
        return linelength;
    }


}