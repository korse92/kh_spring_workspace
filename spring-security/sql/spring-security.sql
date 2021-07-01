--======================================
-- spring-security
--======================================
select
    *
from
    member;

--회원별 복수개의 권한을 관리하는 테이블 authorities
create table authorities(
    id varchar2(20),
    auth varchar2(50), -- ROLE_USER, ROLE_ADMIN, ROLE_SALES, ROLE_HR, ROLE_MANAGER...
    constraint pk_authorities primary key(id, auth),
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
    

-- remember-me 관련테이블 persistent_logins
create table persistent_logins (
    username varchar2(64) not null,
    series varchar2(64) primary key,
    token varchar2(64) not null, -- username, password, expiry time 등을 hashing한 값
    last_used timestamp not null
);

select * from persistent_logins;





