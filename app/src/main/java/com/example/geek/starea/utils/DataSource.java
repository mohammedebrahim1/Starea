package com.example.geek.starea.utils;

import com.example.geek.starea.Models.HeaderTextItem;
import com.example.geek.starea.Models.PostTextItem;
import com.example.geek.starea.Models.TimelineItem;
import com.example.geek.starea.R;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static List<TimelineItem> getTimelineData (){
        List<TimelineItem> mdata = new ArrayList<>();

        //create header item
        HeaderTextItem itemHeader = new HeaderTextItem("اليوم");
        TimelineItem headerTimelineItem = new TimelineItem(itemHeader);

        //create Post Text Item
        PostTextItem postTextItem = new PostTextItem("تقرر عقد الامتحان الثاني لهذا الفصل الدراسي لمقرر هياكل البيانات يوم الخميس الموافق 12/4/2020 في مدرج 1 بالقسم", R.drawable.rob,"18:35");
        TimelineItem postTextTimelineItem = new TimelineItem(postTextItem);


        //create header item
        HeaderTextItem itemHeader1 = new HeaderTextItem("بالأمس");
        TimelineItem headerTimelineItem1 = new TimelineItem(itemHeader1);

        //create Post Text Item
        PostTextItem postTextItem1 = new PostTextItem(" تم رفع المحاضره السابقة ,  download Lec9.ppt ", R.drawable.rob,"14:42");
        TimelineItem postTextTimelineItem1 = new TimelineItem(postTextItem1);


        //create header item
        HeaderTextItem itemHeader2 = new HeaderTextItem("منذ يومين");
        TimelineItem headerTimelineItem2 = new TimelineItem(itemHeader2);

        //create Post Text Item
        PostTextItem postTextItem2 = new PostTextItem("تم رصد درجات الكويز السابق ويمكنكم الاطلاع عليها اعتبارا من اليوم download QuizResults.docx ", R.drawable.rob,"23:15");
        TimelineItem postTextTimelineItem2 = new TimelineItem(postTextItem2);


        //create header item
        HeaderTextItem itemHeader3 = new HeaderTextItem("منذ ثلاثة أيام");
        TimelineItem headerTimelineItem3 = new TimelineItem(itemHeader3);

        //create Post Text Item
        PostTextItem postTextItem3 = new PostTextItem(" تم رفع المحاضره السابقة ,  download Lec8.ppt ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem3 = new TimelineItem(postTextItem3);

        //create header item
        HeaderTextItem itemHeader4 = new HeaderTextItem("منذ اسبوع");
        TimelineItem headerTimelineItem4 = new TimelineItem(itemHeader4);

        //create Post Text Item
        PostTextItem postTextItem4 = new PostTextItem("تم رفع المحاضره السابقة ,  download Lec7.ppt ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem4 = new TimelineItem(postTextItem4);

        //create header item
        HeaderTextItem itemHeader5 = new HeaderTextItem("منذ اسبوعين");
        TimelineItem headerTimelineItem5 = new TimelineItem(itemHeader5);

        //create Post Text Item
        PostTextItem postTextItem5 = new PostTextItem(" تقرر الغاء محاضره مقرر هياكل بيانات يوم الاربعاء 11/3/2020 وذلك لعقد مؤتمر البحوث الطلابية في نفس ميعاد المحاضره ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem5 = new TimelineItem(postTextItem5);

        //create header item
        HeaderTextItem itemHeader6 = new HeaderTextItem("منذ اسبوعين");
        TimelineItem headerTimelineItem6 = new TimelineItem(itemHeader6);

        //create Post Text Item
        PostTextItem postTextItem6 = new PostTextItem(" تم رفع مرجع اضافي للمادة للاطلاع download TextBook2.Pdf ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem6 = new TimelineItem(postTextItem6);

        //create header item
        HeaderTextItem itemHeader7 = new HeaderTextItem("منذ اسبوعين");
        TimelineItem headerTimelineItem7 = new TimelineItem(itemHeader7);

        //create Post Text Item
        PostTextItem postTextItem7 = new PostTextItem(" تم رفع المحاضره السابقة ,  download Lec6.ppt ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem7 = new TimelineItem(postTextItem7);

        //create header item
        HeaderTextItem itemHeader8 = new HeaderTextItem("منذ 3 اسابيع");
        TimelineItem headerTimelineItem8 = new TimelineItem(itemHeader8);

        //create Post Text Item
        PostTextItem postTextItem8 = new PostTextItem(" تقرر عقد امتحان منتصف الفصل الدراسي يوم 31/3/2020 ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem8 = new TimelineItem(postTextItem8);

        //create header item
        HeaderTextItem itemHeader9 = new HeaderTextItem("منذ شهر");
        TimelineItem headerTimelineItem9 = new TimelineItem(itemHeader9);

        //create Post Text Item
        PostTextItem postTextItem9 = new PostTextItem(" تم رفع المحاضره السابقة ,  download Lec5.ppt ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem9 = new TimelineItem(postTextItem9);

        //create header item
        HeaderTextItem itemHeader10 = new HeaderTextItem("منذ شهر");
        TimelineItem headerTimelineItem10 = new TimelineItem(itemHeader10);

        //create Post Text Item
        PostTextItem postTextItem10 = new PostTextItem(" تم رصد درجات الكويز السابق ويمكنكم الاطلاع عليها اعتبارا من اليوم download QuizResults.docx ", R.drawable.rob,"23:15");
        TimelineItem postTextTimelineItem10 = new TimelineItem(postTextItem10);

        //create header item
        HeaderTextItem itemHeader11 = new HeaderTextItem("منذ 5 اساببيع");
        TimelineItem headerTimelineItem11 = new TimelineItem(itemHeader11);

        //create Post Text Item
        PostTextItem postTextItem11 = new PostTextItem("  تم رفع المحاضره السابقة ,  download Lec4.ppt ", R.drawable.rob,"17:22");
        TimelineItem postTextTimelineItem11 = new TimelineItem(postTextItem11);

        //create header item
        HeaderTextItem itemHeader12 = new HeaderTextItem("منذ 6 اسابيع");
        TimelineItem headerTimelineItem12 = new TimelineItem(itemHeader12);

        //create Post Text Item
        PostTextItem postTextItem12 = new PostTextItem("  تم رفع المحاضره السابقة ,  download Lec3.ppt ", R.drawable.rob,"22:55");
        TimelineItem postTextTimelineItem12 = new TimelineItem(postTextItem12);

        //create header item
        HeaderTextItem itemHeader13 = new HeaderTextItem("منذ 7 اسابيع");
        TimelineItem headerTimelineItem13 = new TimelineItem(itemHeader13);

        //create Post Text Item
        PostTextItem postTextItem13= new PostTextItem("  تم رفع المحاضره السابقة ,  download Lec2.ppt ", R.drawable.rob,"19:36");
        TimelineItem postTextTimelineItem13 = new TimelineItem(postTextItem13);

        //create header item
        HeaderTextItem itemHeader14 = new HeaderTextItem("منذ شهرين");
        TimelineItem headerTimelineItem14 = new TimelineItem(itemHeader14);

        //create Post Text Item
        PostTextItem postTextItem14 = new PostTextItem("  تم رفع المرجع الرئيسي للمادة download TextBook.Pdf", R.drawable.rob,"23:45");
        TimelineItem postTextTimelineItem14 = new TimelineItem(postTextItem14);

        //create header item
        HeaderTextItem itemHeader15 = new HeaderTextItem("منذ 9 اسابيع");
        TimelineItem headerTimelineItem15 = new TimelineItem(itemHeader15);

        //create Post Text Item
        PostTextItem postTextItem15 = new PostTextItem("تم رفع المحاضره السابقة , download Lec1.ppt ", R.drawable.rob,"16:09");
        TimelineItem postTextTimelineItem15 = new TimelineItem(postTextItem15);

        //create header item
        HeaderTextItem itemHeader16 = new HeaderTextItem("منذ 10 اسابيع ");
        TimelineItem headerTimelineItem16 = new TimelineItem(itemHeader16);

        //create Post Text Item
        PostTextItem postTextItem16 = new PostTextItem(" تم رفع الملف الخاص بأهداف المقرر , download OverView.Pdf ", R.drawable.rob,"19:55");
        TimelineItem postTextTimelineItem16 = new TimelineItem(postTextItem16);


//
        mdata.add(headerTimelineItem);
        mdata.add(postTextTimelineItem);
        mdata.add(headerTimelineItem1);
        mdata.add(postTextTimelineItem1);
        mdata.add(headerTimelineItem2);
        mdata.add(postTextTimelineItem2);
        mdata.add(headerTimelineItem3);
        mdata.add(postTextTimelineItem3);
        mdata.add(headerTimelineItem4);
        mdata.add(postTextTimelineItem4);
        mdata.add(headerTimelineItem5);
        mdata.add(postTextTimelineItem5);
        mdata.add(headerTimelineItem6);
        mdata.add(postTextTimelineItem6);
        mdata.add(headerTimelineItem7);
        mdata.add(postTextTimelineItem7);
        mdata.add(headerTimelineItem8);
        mdata.add(postTextTimelineItem8);
        mdata.add(headerTimelineItem9);
        mdata.add(postTextTimelineItem9);
        mdata.add(headerTimelineItem10);
        mdata.add(postTextTimelineItem10);
        mdata.add(headerTimelineItem11);
        mdata.add(postTextTimelineItem11);
        mdata.add(headerTimelineItem12);
        mdata.add(postTextTimelineItem12);
        mdata.add(headerTimelineItem13);
        mdata.add(postTextTimelineItem13);
        mdata.add(headerTimelineItem14);
        mdata.add(postTextTimelineItem14);
        mdata.add(headerTimelineItem15);
        mdata.add(postTextTimelineItem15);
        mdata.add(headerTimelineItem16);
        mdata.add(postTextTimelineItem16);

        return mdata;
    }
}
