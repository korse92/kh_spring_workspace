--===========================================
--관리자 계정
--===========================================
create user spring
identified by spring
default tablespace users;

grant connect, resource to spring;

--===========================================
--spring 계정
--===========================================
create table dev(
    no number,
    name varchar2(100) not null,
    career number not null,
    email varchar2(200) not null,
    gender char(1),
    lang varchar2(100) not null,
    constraint pk_dev_no primary key(no),
    constraint ck_dev_gender check(gender in ('M', 'F'))
);

create sequence seq_dev_no;

select * from dev;

create table member(
    id varchar2(20),
    password varchar2(300) not null,
    name varchar2(256) not null,
    gender char(1),
    birthday date,
    email varchar2(256),
    phone char(11) not null,
    address varchar2(512),
    hobby varchar2(256),
    enroll_date date default sysdate,
    enabled number default 1, -- 활성화 여부 : 1(활성화), 0(비활성화)    
    constraint pk_member_id primary key(id),
    constraint ck_member_gander check(gender in ('M', 'F')),
    constraint ck_member_enabled check(enabled in (1, 0))
);

insert into spring.member values ('abcde','1234','아무개','M',to_date('88-01-25','rr-mm-dd'),'abcde@naver.com','01012345678','서울시 강남구','운동,등산,독서',default,default);
insert into spring.member values ('qwerty','1234','김말년','F',to_date('78-02-25','rr-mm-dd'),'qwerty@naver.com','01098765432','서울시 관악구','운동,등산',default,default);
insert into spring.member values ('admin','1234','관리자','F',to_date('90-12-25','rr-mm-dd'),'admin@naver.com','01012345678','서울시 강남구','독서',default,default);
commit;

select * from member order by enroll_date;

DELETE FROM
    member
WHERE
    id in ('assd', 'asdas', 'asdsad', 'ssadas','asdasd','aaasd');

--비밀번호 수동 업데이트
update member
set password = '$2a$10$i70XotxsHF/SDxEYPOit3eEyA8glxOs0bvA8HxLyh4ooQQstZOFLe'
where id = 'abcde';

commit;

--memo테이블 생성
create table memo(
    no number,
    memo varchar2(2000),
    password char(4) not null,
    reg_date date default sysdate,
    constraint pk_memo_no primary key(no)
);

create sequence seq_memo_no;

insert into
    memo
values(
    seq_memo_no.nextval, '안녕하세요, 좋은날입니다.', '1234', default
);
commit;

select * from memo order by no;

-- board 테이블 & attachment 테이블
create table board(
    no number,
    title varchar2(500),
    member_id varchar2(15),
    content varchar2(2000),
    reg_date date default sysdate,
    read_count number default 0,
    constraint pk_board_no primary key(no),    
    constraint fk_board_member_id foreign key(member_id)
                                  references member(id) on delete set null
);

create table attachment(
    no number,
    board_no number not null,
    original_filename varchar2(256) not null,
    renamed_filename varchar2(256) not null,
    upload_date date default sysdate,
    download_count number default 0,
    status char(1) default 'Y',
    constraint pk_attachment_no primary key(no),
    constraint fk_attachment_board_no foreign key(board_no)
                                      references board(no)
                                      on delete cascade,
    constraint ck_attachment_status check(status in ('Y', 'N'))                                    
);

create sequence seq_board_no;
create sequence seq_attachment_no;

Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 1','abcde','반갑습니다',to_date('18/02/10','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 2','qwerty','안녕하세요',to_date('18/02/12','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 3','admin','반갑습니다',to_date('18/02/13','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 4','abcde','안녕하세요',to_date('18/02/14','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 5','qwerty','반갑습니다',to_date('18/02/15','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 6','admin','안녕하세요',to_date('18/02/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 7','abcde','반갑습니다',to_date('18/02/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 8','qwerty','안녕하세요',to_date('18/02/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 9','admin','반갑습니다',to_date('18/02/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 10','abcde','안녕하세',to_date('18/02/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 11','qwerty','반갑습니다',to_date('18/03/11','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 12','admin','안녕하세',to_date('18/03/12','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 13','abcde','반갑습니다',to_date('18/03/13','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 14','qwerty','안녕하세',to_date('18/03/14','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 15','admin','반갑습니다',to_date('18/03/15','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 16','abcde','안녕하세',to_date('18/03/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 17','qwerty','반갑습니다',to_date('18/03/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 18','admin','안녕하세',to_date('18/03/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 19','abcde','반갑습니다',to_date('18/03/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 20','qwerty','안녕하세',to_date('18/03/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 21','admin','반갑습니다',to_date('18/04/01','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 22','abcde','안녕하세',to_date('18/04/02','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 23','qwerty','반갑습니다',to_date('18/04/03','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 24','admin','안녕하세',to_date('18/04/04','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 25','abcde','반갑습니다',to_date('18/04/05','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 26','qwerty','안녕하세',to_date('18/04/06','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 27','admin','반갑습니다',to_date('18/04/07','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 28','abcde','안녕하세',to_date('18/04/08','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 29','qwerty','반갑습니다',to_date('18/04/09','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 30','admin','안녕하세',to_date('18/04/10','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 31','abcde','반갑습니다',to_date('18/04/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 32','qwerty','안녕하세',to_date('18/04/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 33','admin','반갑습니다',to_date('18/04/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 34','abcde','안녕하세',to_date('18/04/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 35','qwerty','반갑습니다',to_date('18/04/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 36','admin','안녕하세',to_date('18/05/01','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 37','abcde','반갑습니다',to_date('18/05/02','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 38','qwerty','안녕하세',to_date('18/05/03','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 39','admin','반갑습니다',to_date('18/05/04','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 40','abcde','안녕하세',to_date('18/05/05','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 41','qwerty','반갑습니다',to_date('18/05/06','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 42','admin','안녕하세',to_date('18/05/07','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 43','abcde','반갑습니다',to_date('18/05/08','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 44','qwerty','안녕하세',to_date('18/05/09','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 45','admin','반갑습니다',to_date('18/05/10','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 46','abcde','안녕하세',to_date('18/05/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 47','qwerty','반갑습니다',to_date('18/05/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 48','admin','안녕하세',to_date('18/05/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 49','abcde','반갑습니다',to_date('18/05/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 50','qwerty','안녕하세',to_date('18/05/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 51','admin','반갑습니다',to_date('18/05/01','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 52','abcde','안녕하세',to_date('18/06/02','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 53','qwerty','반갑습니다',to_date('18/06/03','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 54','admin','안녕하세',to_date('18/06/04','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 55','abcde','반갑습니다',to_date('18/06/05','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 56','qwerty','안녕하세',to_date('18/06/06','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 57','admin','반갑습니다',to_date('18/06/07','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 58','abcde','안녕하세',to_date('18/06/08','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 59','qwerty','반갑습니다',to_date('18/06/09','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,REG_DATE,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 60','admin','안녕하세',to_date('18/06/10','RR/MM/DD'),0);

commit;
select * from board order by no desc;
select * from attachment order by no desc;

desc attachment;

--no 내림차순 페이징 처리

select sequence_name, min_value, max_value, increment_by, last_number, cache_size
from user_sequences;

select
    b.*,
    (select count(*) from attachment where board_no = b.no) attach_count
from
    board b
order by
    no desc;

--샘플 데이터 추가
insert into
    attachment
values(
    SEQ_ATTACHMENT_NO.nextval,
    60,
    'abc.txt',
    '20210426_23458444.txt',
    default,
    default,
    default
);
commit;

--1. rownum
select *
from (
    select rownum rnum, B.*
    from (select *
          from board
          order by no desc
          ) B
    )
where rnum between 6 and 10;

--2. row_number
select *
from (
    select row_number() over(order by no desc) rnum, B.*
    from board B
    )
where rnum between 1 and 5;

-- mybatis collection
-- board(1) : attachment(N) -> 1:N 관계를 하나의 쿼리로 조회
select b.*,
       a.no attach_no,
       a.board_no,
       a.original_filename,
       a.renamed_filename,
       a.upload_date,
       a.download_count,
       a.status
from 
    board b left join
    attachment a
        on b.no = a.board_no
where
    b.no = 88;
    
    
--spring-security
select
    *
from
    member;
    
--회원별 복수개의 권한을 관리하는 테이블 authorities
create table authorities(
    id varchar2(20),
    auth varchar2(50), -- ROLE_USER, ROLE_ADMIN, ROLE_SALES, ROLE_HR, ROLE_MANAGER...
    constraint pk_authorities primary key(id, auth), -- 복합키 사용
    constraint fk_authorities_member_id foreign key(id) references member(id)
);

insert into
    authorities(id, auth)
values(
    'abcde', 'ROLE_USER'
);

insert into
    authorities(id, auth)
values(
    'admin', 'ROLE_USER'
);

insert into
    authorities(id, auth)
values(
    'admin', 'ROLE_ADMIN'
);

select
    *
from
    member
where
    id = 'admin';

select
    *
from
    authorities
where
    id = 'admin';
    
    
-- member - authorities join
select
    *
from
    member m
  left join
    authorities a
        on m.id = a.id
where
    m.id = 'admin';

-- remember-me 관련테이블 persistent_logins
create table persistent_logins (
    username varchar2(64) not null,
    series varchar2(64) primary key,
    token varchar2(64) not null, -- username, password, expiry time 등을 hashing한 값
    last_used timestamp not null
);

select * from persistent_logins;
