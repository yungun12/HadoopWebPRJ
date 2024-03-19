create table HDFS_INFO
(
    FILE_SEQ  NUMBER not null
        constraint HDFS_INFO_PK
            primary key,
    FILE_NAME VARCHAR2(1000),
    FILE_PATH VARCHAR2(1000),
    ORG_NAME  VARCHAR2(100),
    EXT       VARCHAR2(20),
    REG_DT    DATE default SYSDATE
)
/