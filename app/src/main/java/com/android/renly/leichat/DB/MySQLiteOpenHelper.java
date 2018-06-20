package com.android.renly.leichat.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.renly.leichat.Bean.Message;
import com.android.renly.leichat.Bean.User;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    //单例模式
    private static final String DB_NAME = "leichat.db";
    public static final String TABLE_User = "User";
    public static final String TABLE_Message = "Message";
    private static final int VERSION = 1;
    private static MySQLiteOpenHelper instance;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    public synchronized static MySQLiteOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new MySQLiteOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据库创建
        db.execSQL("create table if not exists " + TABLE_User + " (U_id integer primary key autoincrement, " +
                "name char(20), password char(20), headPhoto char(50))");

        db.execSQL("create table if not exists " + TABLE_Message + " (M_id integer primary key autoincrement, " +
        "M_content Text,M_Time Date,M_Status bit,M_messageType integer,M_fromUserID char(20),M_toUserID char(20))");

        //插入初始数据
        insertData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //数据库升级
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    private String headPhoto1 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/Ejo*xAMmPTTStXnvRK.U5xKTxc5uK7vDWGoUUzyN0rs!/b/dC4BAAAAAAAA&bo=gAKAAoACgAIRGS4!&rf=viewer_4";
    private String headPhoto2 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/3NHDgZN9AAxy3RuyvCQGdkjsSKL6n5707Wl5hqY1S5U!/b/dDIBAAAAAAAA&bo=JgImAiYCJgIRCT4!&rf=viewer_4";
    private String img = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/F30x9B4k6qFpl6acDl66bAQ1Lg2BQrvWWP7.SQ8s8uI!/b/dAgBAAAAAAAA&bo=UAFYAVABWAEDCSw!&rf=viewer_4";
    private String img1 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/ZVU219Y5gp2VhDelSYRNr6hA1l3KxRL*UZqj9Bks0VU!/b/dDEBAAAAAAAA&bo=WAJZAlgCWQIRCT4!&rf=viewer_4";
    private String img2 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/hkAN7ZOEiRPDuiQq.ax0IjNFCFaV70x6mr48jjYUhR8!/b/dFkAAAAAAAAA&bo=RAJEAkQCRAIRCT4!&rf=viewer_4";
    private String img3 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/LbTBuD2at8fsZ0q3g7*Ek9ROXDTzCAvecSYwj1nfBYU!/b/dDABAAAAAAAA&bo=LAEsASwBLAERCT4!&rf=viewer_4";
    private void insertData(SQLiteDatabase db){
        db = this.getWritableDatabase();
        db.beginTransaction();

        User user1 = new User("amanda","123",headPhoto1);
        User user2 = new User("samansa","123",headPhoto2);

        //添加两个账户
        db.execSQL(insertSql(user1));
        db.execSQL(insertSql(user2));


        db.setTransactionSuccessful();
        db.endTransaction();


    }

    //插入用户
    public String insertSql(User user) {
        return "insert into " + TABLE_User + "(name,password,headPhoto) values('" +
                user.getName() + "','" + user.getPassword() + "','" + user.getHeadPhoto() + "')";
    }

    //插入好友用户
    public String insertSql(User user,boolean flag){
         return "insert into " + TABLE_User + "(name,headPhoto) values('" +
                user.getName() + "','" + user.getHeadPhoto() + "')";
    }

    //插入消息
    public String insertSql(Message msg) {
        return "insert into " + TABLE_Message + "(M_content, M_Time, M_Status, M_messageType, M_fromUserID, M_toUserID) " +
                "values('" + msg.getContent() + "','" + msg.getTime() + "','" + msg.getType()
                + "','" + msg.getFromUserId() + "','" + msg.getFromUserId() + "')";
    }
}
