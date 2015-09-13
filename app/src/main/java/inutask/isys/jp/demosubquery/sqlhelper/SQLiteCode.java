package inutask.isys.jp.demosubquery.sqlhelper;

import android.database.sqlite.SQLiteQueryBuilder;

import java.util.Arrays;

/**
 * Created by Yellow on 2015/08/09.
 *
 * SQL文を表現するクラス
 * 必要なパラメータを内部で持ち、TableOperatorクラスへ渡すことが目的
 */
public class SQLiteCode {
    private String tables;
    private String[] columns;
    private String where;
    private String operator = null;
    private SQLiteCode subquery;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;
    private boolean distinct = false;
    private boolean isSubquery = false;

    public SQLiteCode(String tables, String[] columns, String where, String groupBy, String having, String orderBy, String limit, boolean distinct) {
        this.tables = tables;
        this.columns = columns;
        this.where = where;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
        this.distinct = distinct;
    }


    // サブクエリ対応
    public SQLiteCode(String tables, String[] columns, String where, String operator, SQLiteCode subquery, String groupBy, String having, String orderBy, String limit, boolean distinct) {
        this.tables = tables;
        this.columns = columns;
        this.where = where;
        this.operator = operator;
        this.subquery = subquery;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
        this.distinct = distinct;

        isSubquery = true;
    }


    public String[] getColumns() {
        return columns;
    }


    public String getWhere() {
        return where;
    }


    public String getGroupBy() {
        return groupBy;
    }


    public String getHaving() {
        return having;
    }


    public String getOrderBy() {
        return orderBy;
    }


    public String getLimit() {
        return limit;
    }



    public SQLiteCode getSubquery() {
        return subquery;
    }

    public String getOperator() {
        return operator;
    }

    public boolean isSubquery() {
        return isSubquery;
    }

    /*
            Android標準のメソッドを利用して、SQLite文を生成する
            サブクエリには未対応.　呼びだされたら""を返す
         */
    @Override
    public String toString() {
        if(isSubquery == false) {
            return SQLiteQueryBuilder.buildQueryString(distinct, tables, columns, where, groupBy, having, orderBy, limit);
        }else{
            return SQLiteQueryBuilder.buildQueryString(distinct, tables, columns, where + " " + operator + " (" + subquery.toString() + ")", groupBy, having, orderBy, limit);
        }
    }

}
