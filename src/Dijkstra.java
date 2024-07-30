import javax.swing.*;
import java.sql.SQLOutput;
import java.util.*;

public class Dijkstra {
    private  String[] str;
    Operation op;

    /**
     * 接收参数
     * 在后面的每一部添加时，将选中的点和线放入一个集合中，再传给画布进行每一步绘画
     * 需要传给算法的参数
     * len 点的个数
     * Dijkstra dijkstra = new Dijkstra(len);
     *
     * weight 数字矩阵
     * str 全部点的名字的数组
     * i 起始点的id
     * e 终点的id
     * dijkstra.dijkstra(weight, str, i,e);
     */

    /**传出参数
     * 传到toDraw
     * dijkstra
     *
     */

    private Queue visited;
    int[] distance;

    MyPoint startPoint;//起点
    MyPoint endPoint;//终点

    private int [][] map;//邻接矩阵
    private ArrayList<MyPoint> points;  //必须
    private ArrayList<Connection> LinesAndDistance;  //必须

    boolean result=true;

    //测试构造
    public Dijkstra(int len) {
        // TODO Auto-generated constructor stub

        visited = new LinkedList();
        distance = new int[len];

    }

    public Dijkstra(AdjacencyMatrix adjacencyMatrix) {
        System.out.println("dijkstra init:");

        this.map=adjacencyMatrix.getMatrix();//获取矩阵
        this.points=adjacencyMatrix.getPoints();

        this.str = toNameArr();
        this.startPoint =MyPoint.findByName(                JOptionPane.showInputDialog("输入起点名字")              ,this.points);
        this.endPoint =MyPoint.findByName(                JOptionPane.showInputDialog("输入终点名字")              ,this.points);

        visited = new LinkedList();
        distance = new int[points.size()];
    }

    private int getIndex(Queue q,int[] dis){
        int k = -1;
        int min_num = Integer.MAX_VALUE;
        for (int i = 0; i < dis.length; i++) {
            if (!q.contains(i)) {
                if (dis[i] < min_num) {
                    min_num = dis[i];
                    k = i;
                }
            }
        }
        return k;
    }

    public void run(){
        dijkstra(map,str,startPoint.id,endPoint.id);
    }

    /**
     *
     * @param weight 矩阵
     * @param str 点名的数组
     * @param v 源点
     * @param e 结束点
     * 保存路径到数组
     */
    public void dijkstra(int[][] weight ,Object[] str,int v,int e){
        HashMap path= new HashMap();
        HashMap id = new HashMap();

        op = new Operation(str.length);

        for (int i = 0; i < str.length; i++){
            path.put(i, "");
            id.put(i,"");
        }

        //初始化路径长度数组distance
        for (int i = 0; i < str.length; i++) {
            path.put(i, path.get(i) + "" + str[v]);
            id.put(i, id.get(i) + "" + str[v]);

            if(i==e){
                op.put(id.get(i)+"");
            }

            if (i == v)
                distance[i] = 0;
            else if (weight[v][i] != -1) {
                distance[i] = weight[v][i];

                path.put(i, path.get(i) + "-->" + str[i]);
                if(i==e){
                        op.put(id.get(i) +""+ str[i]);
                }
                id.put(i, id.get(i) +""+ str[i]);

                /**
                 * TODO
                 */
                System.out.println("indirect:"+path.get(i));
                System.out.println(i+" id:"+id.get(i));
                System.out.println();

            } else
                distance[i] = Integer.MAX_VALUE;
        }
        visited.add(v);
        while (visited.size() < str.length) {
            int k = getIndex(visited, distance);//获取未访问点中距离源点最近的点
            visited.add(k);
            if (k != -1) {

                for (int j = 0; j < str.length; j++) {
                    //判断k点能够直接到达的点
                    if (weight[k][j] != -1) {
                        //通过遍历各点，比较是否有比当前更短的路径，有的话，则更新distance，并更新path。
                        if (distance[j] > distance[k] + weight[k][j]) {
                            distance[j] = distance[k] + weight[k][j];

                            path.put(j, path.get(k) + "-->" + str[j]);

                            //检查是否存在新路径
                            if(j==e){
                                if(id.get(j)!=(id.get(k) +""+ str[j])){
                                    op.put(id.get(k) +""+ str[j]);
                                }
                            }

                            id.put(j, id.get(k) + "" + str[j]);


                            /**
                             * TODO
                             */
                            System.out.println("direct:"+path.get(k));
                            System.out.println(j+" id:"+id.get(j));
                            System.out.println();
                        }
                    }
                }
            }
        }
        System.out.println(id.get(e).toString());
        System.out.println(op);

        System.out.println();
        System.out.println("finish");
            System.out.println(str[v] + "-->" + str[e] + ":" + distance[e] + " ");
            if (distance[e] == Integer.MAX_VALUE){
                System.out.print(str[v] + "-->" + str[e] + "之间没有可通行路径");
                result=false;
            }
            else
                System.out.print(str[v] + "-" + str[e] + "之间有最短路径，具体路径为：" + path.get(e).toString());
            System.out.println();

        visited.clear();
    }

    /**
     * 从点数组获得点名数组
     * @return str
     */
    public String[] toNameArr(){
        String []str = new String[points.size()];
        int i = 0;
        for (MyPoint p :points){
            str[i] = p.name;
            i++;
        }
        return str;
    }

    public static void main(String[] args) {
        int[][] weight = {
                {0, 10, 12, -1, -1, -1},
                {-1, 0, -1, 16, 25, -1},
                {4, 3, 0, 12, -1, 8},
                {-1, -1, -1, 0, 7, -1},
                {-1, -1, -1, -1, 0, -1},
                {-1, -1, -1, 2, -1, 0}};
        String[] str = {"V1", "V2", "V3", "V4", "V5", "V6"};
        int len = str.length;
        Dijkstra dijkstra = new Dijkstra(len);

        //依次让各点当源点，并调用dijkstra函数
        int i=0;
        int e=4;
        //dijkstra.dijkstra(weight, str, i,e);

        int [][] weight0 = {
                {-1,5,-1,-1,1},
                {-1,-1,2,-1,-1},
                {-1,-1,-1,3,-1},
                {-1,-1,-1,-1,-1},
                {-1,-1,2,20,-1}
        };
        String[] str0 = {"a","b","c","d","e"};
        Dijkstra dijkstra1 = new Dijkstra(str0.length);
        dijkstra1.dijkstra(weight0,str0,0,3);
    }
}
