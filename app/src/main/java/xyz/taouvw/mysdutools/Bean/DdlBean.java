package xyz.taouvw.mysdutools.Bean;

public class DdlBean {
    String DDl_name;
    String DDl_detail;
    String DDl_date;
    int day_of_ddl;
    int month_of_ddl;
    int year_of_ddl;
    int hour_of_ddl;
    int minute_of_ddl;

    public DdlBean() {
    }

    public DdlBean(String DDl_name, String DDl_detail, String DDl_date) {
        this.DDl_name = DDl_name;
        this.DDl_detail = DDl_detail;
        this.DDl_date = DDl_date;
    }

    public DdlBean(String DDl_name, String DDl_detail, int day_of_ddl, int month_of_ddl, int year_of_ddl, int hour_of_ddl, int minute_of_ddl) {
        this.DDl_name = DDl_name;
        this.DDl_detail = DDl_detail;
        this.day_of_ddl = day_of_ddl;
        this.month_of_ddl = month_of_ddl;
        this.year_of_ddl = year_of_ddl;
        this.hour_of_ddl = hour_of_ddl;
        this.minute_of_ddl = minute_of_ddl;
        this.generateDate();
    }


    public void generateDate() {
        StringBuilder stringBuilder = new StringBuilder();
        if (month_of_ddl <= 9) {
            stringBuilder.append(0);
        }
        stringBuilder.append(month_of_ddl);
        stringBuilder.append(".");
        if (day_of_ddl <= 9) {
            stringBuilder.append(0);
        }
        stringBuilder.append(day_of_ddl);
        stringBuilder.append("\n");
        if (hour_of_ddl <= 9) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hour_of_ddl);
        stringBuilder.append(":");
        if (minute_of_ddl <= 9) {
            stringBuilder.append(0);
        }
        stringBuilder.append(minute_of_ddl);
        this.DDl_date = stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "DdlBean{" +
                "DDl_name='" + DDl_name + '\'' +
                ", DDl_detail='" + DDl_detail + '\'' +
                ", DDl_date='" + DDl_date + '\'' +
                ", day_of_ddl=" + day_of_ddl +
                ", month_of_ddl=" + month_of_ddl +
                ", year_of_ddl=" + year_of_ddl +
                ", hour_of_ddl=" + hour_of_ddl +
                ", minute_of_ddl=" + minute_of_ddl +
                '}';
    }

    public String getDDl_name() {
        return DDl_name;
    }

    public void setDDl_name(String DDl_name) {
        this.DDl_name = DDl_name;
    }

    public String getDDl_detail() {
        return DDl_detail;
    }

    public void setDDl_detail(String DDl_detail) {
        this.DDl_detail = DDl_detail;
    }

    public String getDDl_date() {
        return DDl_date;
    }

    public void setDDl_date(String DDl_date) {
        this.DDl_date = DDl_date;
    }

    public int getDay_of_ddl() {
        return day_of_ddl;
    }

    public void setDay_of_ddl(int day_of_ddl) {
        this.day_of_ddl = day_of_ddl;
    }

    public int getMonth_of_ddl() {
        return month_of_ddl;
    }

    public void setMonth_of_ddl(int month_of_ddl) {
        this.month_of_ddl = month_of_ddl;
    }

    public int getYear_of_ddl() {
        return year_of_ddl;
    }

    public void setYear_of_ddl(int year_of_ddl) {
        this.year_of_ddl = year_of_ddl;
    }

    public int getHour_of_ddl() {
        return hour_of_ddl;
    }

    public void setHour_of_ddl(int hour_of_ddl) {
        this.hour_of_ddl = hour_of_ddl;
    }

    public int getMinute_of_ddl() {
        return minute_of_ddl;
    }

    public void setMinute_of_ddl(int minute_of_ddl) {
        this.minute_of_ddl = minute_of_ddl;
    }
}
