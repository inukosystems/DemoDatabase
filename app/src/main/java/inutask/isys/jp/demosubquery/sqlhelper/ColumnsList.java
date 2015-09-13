package inutask.isys.jp.demosubquery.sqlhelper;

/**
 * Created by Yellow on 2015/05/24.
 * DBのカラムを列挙するクラス
 */

import android.provider.BaseColumns;

public class ColumnsList {


    //
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "SuperRobotBX";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_NULLABLE = null;
    }

}
