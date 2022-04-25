package xyz.taouvw.mysdutools.Bean;

import java.util.Arrays;

/**
 * 教师实体类
 * 参数:教室名
 * 查询日期
 * 有课时间
 * 无课时间
 * 占用时间
 */
public class ClassRoomDetail {
    private String name;
    private int searchDay;
    private int[] freeTime = new int[]{0, 0, 0, 0, 0};
    private int[] occupiedTime = new int[]{0, 0, 0, 0, 0};

    public ClassRoomDetail() {
    }

    public void addfreeTime(int j) {
        freeTime[j-1] = 1;
    }

    public void addoccupiedTime(int j) {
        occupiedTime[j-1] = 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSearchDay() {
        return searchDay;
    }

    public void setSearchDay(int searchDay) {
        this.searchDay = searchDay;
    }

    @Override
    public String toString() {
        return "ClassRoomDetail{" +
                "name='" + name + '\'' +
                ", 哪周=" + searchDay +
                ", 空闲时间=" + Arrays.toString(freeTime) +
                ", 有课时间=" + Arrays.toString(occupiedTime) +
                '}';
    }

    public int[] getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int[] freeTime) {
        this.freeTime = freeTime;
    }

    public int[] getOccupiedTime() {
        return occupiedTime;
    }

    public void setOccupiedTime(int[] occupiedTime) {
        this.occupiedTime = occupiedTime;
    }

}
