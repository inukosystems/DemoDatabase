package inutask.isys.jp.demodatabase.sqlhelper;

/**
 * Created by Yellow on 2015/05/24.
 * DBのカラムを列挙するクラス
 */

import android.provider.BaseColumns;

public class FeedReaderContract {
    public FeedReaderContract(){}

    //
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_NULLABLE = null;
    }


}
