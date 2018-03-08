package cocoelf.codeelfandroid.util;

/**
 * Created by green-cherry on 2018/3/8.
 */

public enum Week {
    Mon("周一",1),Tues("周二",2),Wedn("周三",3),Thus("周四",4),Fri("周五",5),Sat("周六",6),Sun("周日",7);

    String day;
    int dayInint;

     Week(String s,int i){
        this.day=s;
        this.dayInint=i;
    }

    public static String intToString(int i){
         for (Week week:Week.values()){
             if (week.dayInint==i)
                 return week.day;
         }
         return "";
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
