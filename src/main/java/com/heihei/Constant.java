package com.heihei;

public class Constant {

    public static final  String tablespace = "select c.tablespace_name tablespace_name, round(a.bytes/1048576,2) total_mb,round((a.bytes-b.bytes)/1048576,2) used_mb,round(b.bytes/1048576,2) free_mb, round(b.bytes/a.bytes * 100,2)||'%' free_pct from (select tablespace_name,sum(a.bytes) bytes from sys.dba_data_files a group by tablespace_name) a, (select a.tablespace_name, nvl(sum(b.bytes),0) bytes from sys.dba_data_files a, sys.dba_free_space b where a.tablespace_name = b.tablespace_name (+) and a.file_id = b.file_id (+) group by a.tablespace_name) b, sys.dba_tablespaces c where a.tablespace_name = b.tablespace_name(+) and a.tablespace_name = c.tablespace_name order by free_pct";
    public static final  String datafile="select sum(bytes)/1024/1024/1024 GB from dba_data_files";
    public static final String segment="select sum(bytes)/1024/1024/1024 GB from dba_data_files";
    public static final String session="select * from (select count(SESSION_ID),to_char(sample_time,'RR-MON-DD hh24') from dba_hist_active_sess_history group by to_char(sample_time,'RR-MON-DD hh24') order by 1 desc) where rownum < 6";
    public static final String avtiveSession="select * from (select count(SESSION_ID),to_char(sample_time,'RR-MON-DD hh24') from dba_hist_active_sess_history group by to_char(sample_time,'RR-MON-DD hh24') order by 1 desc) where rownum < 6";
    public static final String sessionGroupUser="select username,count(username) from v$session where username is not null group by username";
    public static final String memory="select name ,value , display_value,DESCRIPTION from v$parameter where name like '%memory%'";
    public static final String sga="select  name ,value , display_value,DESCRIPTION from v$parameter where name like '%sga%'" ;
    public static final String pga="select name ,value , display_value,DESCRIPTION from v$parameter where name like '%pga%'";
    public static final String databaseName="select name from v$database";
    public static final String instanceName="select instance_name from gv$instance";
    public static final String version="select * from v$version";
    public static final String psu="select * from dba_registry_history";
    public static final String dbid="select dbid from v$database";
    public static final String controlfile="select name from v$controlfile";
    public static final String resourceLimit="select * from gv$resource_limit";
    public static final String cahrt="select value from v$nls_parameters where parameter='NLS_CHARACTERSET'";
    public static final String ncahrt="select value from v$nls_parameters where parameter='NLS_NCHAR_CHARACTERSET'";
    public static final String logSize="select distinct(bytes/1024/1024) MB from v$log";
    public static final String logCount="select count(1) from v$log";
    public static final String logMember="select distinct(members) from v$log";
    public static final String logMode="select log_mode from v$database";
    public static final String archLog="archive log list";
    public static final String archFile="select value from v$parameter where name = 'log_archive_dest_1'";
    public static final String envnt="select * from (select event,time_waited_fg/1048576 from v$system_event order by time_waited_fg desc) where rownum < 11";
    public static final String isBadBlock="select * from v$database_block_corruption";
    public static final String instanceStatus="select status from v$instance";

    //系统
    public static final String lsnrctl="source /home/oracle/.bash_profile && lsnrctl status | grep Start";
    public static final String catMemory=" free -m | grep Mem |awk  '{printf(\"%s,%s,%.2f\",$3-$7,$2,($3-$7)/$2*100)}'";
    public static final String catCpu="vmstat |awk 'NR==3{print $15}'";
    public static final String disk= "df -Pm| awk 'NR>1{print $1\",\"$2\",\"$3\",\"$4\",\"$5\",\"$6}'";
}
