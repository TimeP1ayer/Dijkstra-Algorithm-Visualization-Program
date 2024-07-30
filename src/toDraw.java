import java.util.ArrayList;
import java.util.Arrays;

public class toDraw {
    Operation operation;//调用operation.op
    String[] bluePath;//全部路径
    String[] redPath;//最终路径
    ArrayList<MyPoint> points = new ArrayList<>();//保存点，用来绘制画面
    ArrayList<MyPoint[]> blueLines = new ArrayList<>();//保存蓝线，用来绘制画面
    ArrayList<MyPoint[]> redLines = new ArrayList<>();//保存红线，用来绘制画面

    /**
     * 传参数到demoPanel
     * blueLines
     * redLines
     * 接收参数
     * Dijkstra
     * points点集合
     */

    /**
     * 路径总数
     */
    public int getTotalPathLength(){
        /**
         * 蓝色路径单独绘出+红色路径一次性绘出
         */
        return blueLines.size()+1;
    }

    /**生成路径
     * 将路径文本转换为路径
     * 并且设置最终路径
     */
    public String[] divide(String[] path){
        int length=path.length;
        System.out.println("path.length:"+path.length);
        String[] finish = new String[length*length];

        int j=0;
        for (int i = 0; i < path.length; i++) {
            if(path[i]!=null){
                String s1 = path[i];
                if(s1.length()==1){
                    continue;
                }
                //对单字符串生成路径
                for (int k =0;k<s1.length()-1;k++){
                    /**
                     * 防止重复
                     */
                    int repeat=0;
                    for (int l = 0; l < finish.length; l++) {
                        if(s1.substring(k,k+2).equals(finish[l])){
                            repeat=1;
                            break;
                        }
                    }
                    if(repeat==1){
                        repeat=0;
                        continue;
                    }

                    finish[j] = s1.substring(k,k+2);
                    j++;
                }
            }else {
                break;
            }
            /**搜寻最终路径
             * TODO
             */
            System.out.println("i: "+i+"path:"+path[i]);
            if(  path[i+1]==null){
                redPath = new String[path[i].length()-1];
                String a = path[i];
                for (int k = 0; k< redPath.length; k++){
                    System.out.println("redPath:"+k+" "+a.substring(k,k+2));
                    redPath[k]=a.substring(k,k+2);
                }
            }

        }
        String[] finishpath = new String[j];
        for (int i = 0; i < j; i++) {
            finishpath[i]=finish[i];
        }

        this.bluePath =finishpath;
        return finishpath;
    }

    /**
     * 构造方法
     * @param dijkstra
     */
    public toDraw(Dijkstra dijkstra,ArrayList<MyPoint> points){
        operation =dijkstra.op;
        this.points=points;
    }
    public  toDraw(){

    }

    /**
     * 将数组转换为线组合
     * @param Path
     * @param Lines
     */
    public void PathToLines(String[] Path, ArrayList<MyPoint[]> Lines) {
        for (int i = 0; i < Path.length; i++) {
            MyPoint start=MyPoint.findByName(Path[i].substring(0,1),points);
            MyPoint end=MyPoint.findByName(Path[i].substring(1,2),points);
            Lines.add(new MyPoint[]{start,end});
        }
    }

    /**
     * 初始化
     */
    public void init(){
        divide(operation.op);
        System.out.println(Arrays.toString(redPath));
        System.out.println(Arrays.toString(bluePath));
        PathToLines(redPath,redLines);
        PathToLines(bluePath,blueLines);
    }

    public static void main(String[] args) {
        String[] op={ "a","aed","aecd" };
        toDraw toDraw = new toDraw();
        System.out.println(Arrays.toString(toDraw.divide(op)));
        System.out.println("final");
        System.out.println(Arrays.toString(toDraw.redPath));
    }

}
