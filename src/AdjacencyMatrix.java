import javax.swing.*;
import java.util.*;

//邻接矩阵
public class AdjacencyMatrix {
    private  int[][] matrix;  //必须
    private ArrayList<MyPoint> points;  //必须
    ArrayList<String> pointNames = new ArrayList<>();//保存点的名字
    ArrayList<MyPoint[]> lines = new ArrayList<>();//保存线，用来绘制画面
    ArrayList<Integer>linelength =new ArrayList<>();//按lines的顺序保存线条长度

    private ArrayList<Connection> LinesAndDistance;  //必须

    public AdjacencyMatrix() {
    }

    /**
     * 点输入
     */
    public void addVertex(int pointLength) {
        //绘制第一个点的位置
        points = new ArrayList<>(pointLength);
        int startX=60;
        int startY=60;
        int count=1;
        /**
         * 设置点的默认位置
         */
        for(int i=0,j=1;i<pointLength;i++,j++){

            MyPoint point = new MyPoint();
            point.x=startX;
            point.y=startY;
            point.id=i;

            //输入点的名字
            String name = JOptionPane.showInputDialog("输入第"+(i+1)+"个点的名字:");
            point.name  = name;

            if(j%2==0){
                count+=1;
                //重新定位左侧第一个点
                if (count%2!=0){
                    startX=60;
                }else{
                    startX=80;
                }
                startY+=60;
            }else {
                startX+=60;
            }

            points.add(point);
            pointNames.add(name != null ? name : "");

            System.out.println(points.get(i).id+" locate:"+points.get(i).x+":"+points.get(i).y);
        }
    }

    /**
     * 线输入
     */
    public void addEdge(int lineLength) {
        ArrayList<Connection> LinesAndDistance = new ArrayList<>();
        //输入点与点之间的信息
        for (int i=0;i<lineLength;i++){
            CustomInputDialog dialog = new CustomInputDialog(null);
            String[] inputs = dialog.showDialog();
            /**
             * 寻找点的位置
             */
            MyPoint start = MyPoint.findByName(inputs[0],this.points);
            MyPoint end = MyPoint.findByName(inputs[1],this.points);
            if (start!=null&&end!=null){
                Connection c= new Connection(start,end,  Integer.parseInt(inputs[2])   );
                LinesAndDistance.add(c);
                //添加到线长数组
                lines.add(new MyPoint[]{start,end});
                linelength.add(Integer.parseInt(inputs[2]));
            }
        }
        this.LinesAndDistance=LinesAndDistance;
    }

    /**
     * 由键盘输入初始化邻接矩阵
     */
    public boolean initByKey(PointAndLineDrawer pad){
        int pointLength=Integer.parseInt(JOptionPane.showInputDialog("输入的点数:"));
        int lineLength=Integer.parseInt(JOptionPane.showInputDialog("输入的边数:"));
        if(pointLength<=1){
            JOptionPane.showMessageDialog(pad,"算法演示需要两个点或以上");
            return false;
        }
        addVertex(pointLength);
        addEdge(lineLength);
        init(pointLength,this.points,this.LinesAndDistance);
        return true;
    }

    /**
     * 初始化邻接矩阵
     * @param PointTurn
     * @param points
     * @param LinesAndDistance
     */
    public void init(int PointTurn, ArrayList<MyPoint> points, ArrayList<Connection> LinesAndDistance){
        this.points=points;
        this.LinesAndDistance=LinesAndDistance;

        matrix = new int[PointTurn][PointTurn];

        //默认不连通，距离-1
        for (int i = 0; i < PointTurn; i++) {
            for (int j = 0; j < PointTurn; j++) {
                matrix[i][j]=-1;
            }
        }

        String []pointsName = new String[PointTurn];
        /**
         * 将点的名字存到pointsName
         */
        int i=0;
        for (MyPoint p : points){
            pointsName[i] = p.name;
            i++;
            if(i>=PointTurn)break;
        }
        /**
         * 每个MyPoint都有id，对应PointTurn,id从0开始
         */
        /**
         * 给矩阵赋值距离
         * 起点->终点，左起点，右终点
         */
        for (Connection c : LinesAndDistance){
            matrix[c.getStart().id][c.getEnd().id] = c.getDistance();
        }
    }

    /**
     * 输出矩阵
     */
    public String MatrixString(){
        StringBuilder sb = new StringBuilder();
        sb.append("   ");//3
        for (int i=0;i<points.size();i++){
            sb.append( centerPrint(5," ",points.get(i).name)  );
        }
        sb.append("\n");
        for (int i =0 ;i<matrix.length;i++){
            sb.append(  centerPrint(3," ",points.get(i).name)   );
            for (int j=0;j< matrix.length;j++){
                sb.append(  centerPrint(5," ", String.valueOf(matrix[i][j]))    );
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 输出指定位数的居中文本，用空格填补
     * @param length
     * @param replace
     * @param params
     * @return
     */
    public static String centerPrint(int length,String replace,String params){
         int left=(  length-params.length()  )/2;
         int right = length - left - params.length();
         StringBuilder sb = new StringBuilder();
         for (int i = 0;i<left;i++){
             sb.append(replace);
         }
         sb.append(params);
        for (int i = 0;i<right;i++){
            sb.append(replace);
        }
        return sb.toString();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public ArrayList<MyPoint> getPoints() {
        return points;
    }

}