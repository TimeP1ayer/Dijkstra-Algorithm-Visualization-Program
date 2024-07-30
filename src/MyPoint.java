import java.awt.*;
import java.util.ArrayList;

public class MyPoint extends Point {
    String name;
    int id;//id从0开始

    @Override
    public String toString() {
        return "MyPoint{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    /**通过点的X和Y轴进行模糊查找
     * 距离范围在正负10内均可视为同一个点
     * @param p     用户在屏幕上给出的点
     * @param points  已存在的点的组合
     * @return
     */
    public static MyPoint findByPoint(Point p, ArrayList<MyPoint>  points) {
        for (MyPoint aim :points){
            if (aim.x<p.x+15&&aim.x>p.x-15){
                if (aim.y<p.y+15 && aim.y>p.y-15){
                    return aim;
                }
            }
        }
        return null;
    }

    /**
     * 将属性从Point克隆到MyPoint
     * @param p
     * @param name
     * @param id
     * @return
     */
    public static MyPoint Clone (Point p,String name,int id){
        MyPoint result = new MyPoint();
        result.x = p.x;
        result.y = p.y;
        result.name = name;
        result.id = id;
        return result;
    }

    /**
     * 通过点的name属性查找
     * @param pointName
     * @param points
     * @return
     */
    public static MyPoint findByName(String pointName, ArrayList<MyPoint> points) {
        for(int i =0;i<points.size();i++){
            if(pointName.equals(points.get(i).name)){
                return points.get(i);
            }
        }
        return null;
    }

    /**
     * 通过点的id属性查找
     * @param id
     * @param points
     * @return
     */
    public static MyPoint findById(int id, ArrayList<MyPoint> points) {
        for(int i =0;i<points.size();i++){
            if(id==points.get(i).id){
                return points.get(i);
            }
        }
        return null;
    }
}
